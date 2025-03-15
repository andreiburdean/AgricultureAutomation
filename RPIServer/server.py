import aiohttp
from aiohttp import web
import asyncio
import json

test_data: int = 0


async def index(request):
    html_content = """
    <!doctype html>
    <html>
      <head>
        <title>RPi WebSocket Server</title>
      </head>
      <body>
        <h1>RPi WebSocket Server GUI</h1>
        <p>This page is served over HTTP. Open your browser console to see WebSocket messages.</p>
      </body>
    </html>
    """
    return web.Response(text=html_content, content_type="text/html")


async def websocket_handler(request):
    ws = web.WebSocketResponse()
    await ws.prepare(request)

    print("WebSocket client connected")

    async for msg in ws:
        if msg.type == web.WSMsgType.TEXT:
            print("Received sensor data:", msg.data)
            spring_response = await handle_send(request, msg.data)
            # await ws.send_str("Ack: " + msg.data)
        elif msg.type == web.WSMsgType.ERROR:
            print("WebSocket connection closed with exception", ws.exception())

    print("WebSocket client disconnected")
    return ws


async def handle_post(request):
    try:
        data = await request.json()
        test_data = data['value']
        print("Received POST data:", data)
        # spring_response = await handle_send(request, test_data)
        return web.json_response({'status': 'success', 'spring_response': data})
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)


async def send_data_to_spring(data):
    spring_url = "http://192.168.1.128:8080/api/users/test-receive"
    async with aiohttp.ClientSession() as session:
        async with session.post(spring_url, json=data) as response:
            resp_text = await response.text()
            return resp_text


async def handle_send(request, data):
    #     data_to_send = data + 10
    data_to_send = data
    response_text = await send_data_to_spring(data_to_send)
    return web.Response(text=f"Data sent to Spring. Spring responded: {response_text}")


app = web.Application()
app.router.add_get('/', index)
app.router.add_get('/ws', websocket_handler)
app.router.add_post('/data', handle_post)

if __name__ == '__main__':
    web.run_app(app, host="0.0.0.0", port=5000)

