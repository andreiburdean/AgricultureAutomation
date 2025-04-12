import aiohttp
from aiohttp import web
import asyncio
import json
from data_maps import *


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
    spring_url = "http://192.168.1.129:8080/api/sensor-data/receive-sensor-data"
    async with aiohttp.ClientSession() as session:
        async with session.post(spring_url, json=data) as response:
            resp_text = await response.text()
            return resp_text


async def handle_control_command(request):
    try:
        control_data = await request.json()
        print("Received control data:", control_data)
        manual_control_map["switch_control"] = control_data["switch_control"]
        manual_control_map["fan"] = control_data["fan"]
        manual_control_map["pump"] = control_data["pump"]
        manual_control_map["led"] = control_data["led"]
        return web.json_response({'status': 'success', 'spring_response': control_data})
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)


async def handle_modify_custom_program(request):
    try:
        custom_data = await request.json()
        print("Received custom data:", custom_data)
        sensor_control_map["temperature"] = custom_data["temperature"]
        sensor_control_map["humidity"] = custom_data["humidity"]
        sensor_control_map["luminosity"] = custom_data["luminosity"]
        sensor_control_map["soilMoisture"] = custom_data["soilMoisture"]
        return web.json_response({'status': 'success', 'spring_response': custom_data})
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)
