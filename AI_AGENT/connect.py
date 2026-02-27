import json
import requests

#这个文件是在coze平台上已经完成部署的agent的链接代码，直接运行即可看到输出结果
#上面的projects是平台上完整的代码，在readme里有部署教程
url = "https://fpcq97v37d.coze.site/stream_run"
headers = {
  "Authorization": "Bearer <Token>",
  "Content-Type": "application/json",
  "Accept": "text/event-stream",
}

#my token：
#eyJhbGciOiJSUzI1NiIsImtpZCI6ImY2MjJjYjljLThkZjYtNDRlNy04Y2ZmLTNmZTY0MDU4OWVkZSJ9.eyJpc3MiOiJodHRwczovL2FwaS5jb3plLmNuIiwiYXVkIjpbIkMwMEJDRkpNNWtKOVR4dzluVDR5RExFQUZpRVA4ckE5Il0sImV4cCI6ODIxMDI2Njg3Njc5OSwiaWF0IjoxNzcyMTk5MjYzLCJzdWIiOiJzcGlmZmU6Ly9hcGkuY296ZS5jbi93b3JrbG9hZF9pZGVudGl0eS9pZDo3NjExNTE1OTA5NTkyOTA3ODM5Iiwic3JjIjoiaW5ib3VuZF9hdXRoX2FjY2Vzc190b2tlbl9pZDo3NjExNTM3ODc3MjkzMzM0NTcxIn0.gkX7Ndx_E2kyYuU2fvYfAapBPoAgZeApVKDnqru1mGXlAcePt6SzTZxL-LfUhDQC0AOirtESQYfMHuBWqYsl4zcVhUKLPAuztMwZwXkL4FQoyGkSFUEvpVgUwuPYuPr3jPxU1b-2HK-PnzwPhI3Gi19UOWxZDltREzt5dffiCSbHBDbO504W0W5jTje6K_tUViVLHMUfiQbOKBZ2tW1tapsqo5ahTzsiWB5oRtRkDpJEqLAi8SfGpGVCxn-4eBMbeZ_rizh8ZUOwM9ell6Aiw-t3yZ-IuduATA2cGL6QrRqdkO8TTkH7kB4mVTbizu0f_sjOv3QfLOag78nao9smNA
#需要用的时候把我的token替换掉请求头里的<Token>即可

payload = json.loads(r'''{
  "content": {
    "query": {
      "prompt": [
        {
          "type": "text",
          "content": {
            "text": "<此处填写输入内容>"
          }
        }
      ]
    }
  },
  "type": "query",
  "session_id": "LDgNJM49pgs0DUvAOyDLI",
  "project_id": "7611509362124701732"
}''')


response = requests.post(url, headers=headers, json=payload, stream=True)
print("status:", response.status_code)
try:
  response.raise_for_status()
except Exception:
  print(response.text)
  raise
for line in response.iter_lines(decode_unicode=True):
  if line and line.startswith("data:"):
    data_text = line[5:].strip()
    try:
      parsed = json.loads(data_text)
      print(json.dumps(parsed, ensure_ascii=False, indent=2))
    except Exception:
      print(data_text)