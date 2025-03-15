import aiohttp
import request_handlers
from aiohttp import web
from request_handlers import *
import asyncio
import json

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

app = web.Application()
app.router.add_get('/', index)
app.router.add_get('/ws', request_handlers.websocket_handler)
app.router.add_post('/data', request_handlers.test_handle_post)
app.router.add_post('/receive-sensor-data', request_handlers.handle_sensor_data_receive)


if __name__ == '__main__':
    web.run_app(app, host="0.0.0.0", port=5000)