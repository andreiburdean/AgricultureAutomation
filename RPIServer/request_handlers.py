import aiohttp
from aiohttp import web
import asyncio
import json


sensor_data_map = {
    "temperature": 0,
    "humidity": 0,
    "pressure": 0,
    "luminosity": 0,
    "soilMoisture": 0
}

async def handle_sensor_data_receive(request):
    try:
        sensor_data = await request.json()
        print("Received sensor data:", sensor_data)
        sensor_data_map["temperature"] = sensor_data["temperature"]
        sensor_data_map["humidity"] = sensor_data["humidity"]
        sensor_data_map["pressure"] = sensor_data["pressure"]
        sensor_data_map["luminosity"] = sensor_data["luminosity"]
        sensor_data_map["soilMoisture"] = sensor_data["soilMoisture"]
        spring_response = await handle_sensor_data_send(request, sensor_data)
        return web.json_response({'status': 'success', 'spring_response': sensor_data})
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)

async def handle_sensor_data_send(request, data):
    data_to_send = data
    response_text = await send_sensor_data_to_spring(data_to_send)
    return web.Response(text=f"Data sent to Spring. Spring responded: {response_text}")

async def send_sensor_data_to_spring(data):
    spring_url = "http://192.168.1.128:8080/api/sensor-data/receive-sensor-data"
    async with aiohttp.ClientSession() as session:
        async with session.post(spring_url, json=data) as response:
            resp_text = await response.text()
            return resp_text
