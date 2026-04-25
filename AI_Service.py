import uvicorn
from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from neo4j import GraphDatabase
import requests
import re
import base64

app = FastAPI(title="Inaudible Voice AI Service")

BAIDU_AK = "qm9PQkpmHxNcZ9muPvgxym29"
BAIDU_SK = "g1GMnvBGyng53ihI3BU8eazJUDMIDXU2"

DEEPSEEK_KEY = "sk-d5db37f09e7648999e8a415693add4c0"
DEEPSEEK_URL = "https://api.deepseek.com/v1/chat/completions"

NEO4J_URI = "neo4j+s://b84abc8b.databases.neo4j.io"
NEO4J_USER = "neo4j"
NEO4J_PWD = "RIssSnTGdtSQUUPxSkE5xxRc7NRxU4w-uPjsjZZDkLM"

try:
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PWD))
except Exception as e:
    print(f"Neo4j 连接预警: {e}")



def get_baidu_token():
    url = f"https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={BAIDU_AK}&client_secret={BAIDU_SK}"
    try:
        res = requests.get(url).json()
        return res.get("access_token")
    except:
        return None

def ocr_image_to_text(image_bytes):
    token = get_baidu_token()
    if not token: return "OCR授权失败"
    
    url = f"https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token={token}"
    img_64 = base64.b64encode(image_bytes).decode('utf-8')
    payload = {"image": img_64}
    headers = {"Content-Type": "application/x-www-form-urlencoded"}
    
    try:
        res = requests.post(url, data=payload, headers=headers).json()
        return "".join([line["words"] for line in res.get("words_result", [])])
    except:
        return "文本识别解析异常"

def query_metaphors(text):
    metaphors = []
    try:
        with driver.session() as session:
            result = session.run("MATCH (k:Keyword)-[:REFLECTS]->(d:Dimension) RETURN k.name AS word, d.type AS dim")
            for record in result:
                if record["word"] in text:
                    metaphors.append(f"{record['word']}({record['dim']})")
    except:
        pass
    return metaphors



@app.post("/analyze")
async def analyze_essay(
    student_id: str = Form(...), 
    text: str = Form(None), 
    file: UploadFile = File(None)
):
    content = text if text else ocr_image_to_text(await file.read())
    
    if not content or len(content) < 5:
        raise HTTPException(status_code=400, detail="作文内容过短或解析失败")

    # 知识图谱
    hits = query_metaphors(content)
    
    # 调DeepSeek
    prompt = f"""
    [身份]: 小学生心理健康评估专家
    [输入作文]: "{content}"
    [图谱命中关键词]: {hits}
    
    [任务]: 
    请根据作文内容与隐喻词，从以下四个维度进行 0.0 到 1.0 的量化评分：
    1. 学业压力 (Academic)
    2. 人际关系 (Social)
    3. 情绪状态 (Emotion)
    4. 自我认知 (Self)
    
    [要求]: 
    - 给出约 150 字的诊断简报。
    - 严格以该格式结尾：[RESULT] 学业:分数, 人际:分数, 情绪:分数, 自我:分数
    """
    
    try:
        resp = requests.post(
            DEEPSEEK_URL,
            headers={"Authorization": f"Bearer {DEEPSEEK_KEY}"},
            json={"model": "deepseek-chat", "messages": [{"role": "user", "content": prompt}]},
            timeout=30
        ).json()
        full_analysis = resp['choices'][0]['message']['content']
    except:
        full_analysis = "AI 诊断暂时不可用 [RESULT] 学业:0.5, 人际:0.5, 情绪:0.5, 自我:0.5"

    score_match = re.search(r"学业:(\d\.\d+), 人际:(\d\.\d+), 情绪:(\d\.\d+), 自我:(\d\.\d+)", full_analysis)
    s = [float(score_match.group(i)) for i in range(1, 5)] if score_match else [0.5, 0.5, 0.5, 0.5]

    return {
        "studentId": student_id,
        "content": content,
        "scores": {"academic": s[0], "social": s[1], "emotion": s[2], "self": s[3]},
        "expertReport": full_analysis.split("[RESULT]")[0].strip(),
        "isWarning": s[2] > 0.7 or s[0] > 0.8  # 情绪或学业压力过高自动触发预警
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
