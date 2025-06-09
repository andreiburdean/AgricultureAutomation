import aiohttp
from aiohttp import web
import asyncio
import json
from data_maps import *

SERVER_ADDRESS: str = "http://192.168.108.113:8080/"

#receives the sensor readings from the ESP8266 sensor hub
async def handle_sensor_data_receive(request):
    try:
        sensor_data = await request.json()
        print("Received sensor data:", sensor_data)
        sensor_data_map["temperature"] = sensor_data["temperature"]
        sensor_data_map["humidity"] = sensor_data["humidity"]
        sensor_data_map["pressure"] = sensor_data["pressure"]
        sensor_data_map["luminosity"] = sensor_data["luminosity"]
        sensor_data_map["soil_moisture"] = sensor_data["soilMoisture"]
        _ = await handle_sensor_data_send(request, sensor_data)
        return web.json_response({'status': 'success', 'spring_response': sensor_data})
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)

#sends the sensor readings to the applciation server
async def handle_sensor_data_send(_, data):
    data_to_send = data
    response_text = await send_sensor_data_to_spring(data_to_send)
    return web.Response(text=f"Data sent to Spring. Spring responded: {response_text}")

async def send_sensor_data_to_spring(data):
    spring_url = SERVER_ADDRESS + "api/rpi/" + str(RASPBERRY_ID) + "/receive-sensor-data"
    async with aiohttp.ClientSession() as session:
        async with session.post(spring_url, json=data) as response:
            resp_text = await response.text()
            return resp_text

#receives the manual control command from the application server
async def handle_control_command(request):
    try:
        control_data = await request.json()
        print("Received control data:")
        print(control_data)
        raspberry_id = int(request.match_info["raspberry_id"])
        if raspberry_id == RASPBERRY_ID:
            manual_control_map["switch_control"] = control_data["switchControl"]
            manual_control_map["fan"] = control_data["fan"]
            manual_control_map["pump"] = control_data["pump"]
            manual_control_map["led"] = control_data["led"]
            return web.json_response({'status': 'success', 'spring_response': control_data})
        else:
            web.json_response({'status': 'error', 'message': str(e)}, status=400)
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)

#receives the new parameters for the custom program if any is running at that time
async def handle_modify_custom_program_parameters(request):
    try:
        modified_parameters_data = await request.json()
        print("Received modified parameters data:")
        print(modified_parameters_data)
        raspberry_id = int(request.match_info["raspberry_id"])
        if raspberry_id == RASPBERRY_ID:
            sensor_control_map["temperature"] = modified_parameters_data["temperature"]
            sensor_control_map["humidity"] = modified_parameters_data["humidity"]
            sensor_control_map["luminosity"] = modified_parameters_data["luminosity"]
            sensor_control_map["soil_moisture"] = modified_parameters_data["soilMoisture"]
            return web.json_response({'status': 'success', 'spring_response': modified_parameters_data})
        else:
            return web.json_response({'status': 'error', 'message': str(e)}, status=400)
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)

#receives the command that stops the running program
async def handle_program_stop(request):
    try:
        program_stop = await request.json()
        print("Received program stop.")
        raspberry_id = int(request.match_info["raspberry_id"])
        if raspberry_id == RASPBERRY_ID:
            sensor_control_map["succesful_server_startup_response"] = program_stop
            return web.json_response({'status': 'success', 'spring_response':program_stop})
        else:
            return web.json_response({'status': 'error', 'message': str(e)}, status=400)
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)

#receives the command that starts the desired program
async def handle_program_start(request):
    try:
        program_start = await request.json()
        print("Received program start.")
        raspberry_id = int(request.match_info["raspberry_id"])
        if raspberry_id == RASPBERRY_ID:
            sensor_control_map["succesful_server_startup_response"] = program_start
            sensor_control_map["temperature"] = program_start["temperature"]
            sensor_control_map["humidity"] = program_start["humidity"]
            sensor_control_map["luminosity"] = program_start["luminosity"]
            return web.json_response({'status': 'success', 'spring_response':program_start})
        else:
            return web.json_response({'status': 'error', 'message': str(e)}, status=400)
    except Exception as e:
        print("Error processing POST data:", e)
        return web.json_response({'status': 'error', 'message': str(e)}, status=400)

#gets the necessary startup information from the server
async def get_startup_info_from_server(raspberry_id):
    spring_url = SERVER_ADDRESS + "api/rpi/" + str(RASPBERRY_ID) + "/on-startup"
    timeout = aiohttp.ClientTimeout(total = 5, connect = 3, sock_read = 3, sock_connect = 3)
    async with aiohttp.ClientSession(timeout = timeout) as session:
        while True:
            try:
                async with session.post(spring_url, json=raspberry_id) as response:
                    if response.status == 200:
                        response_data = await response.json()
                        if response_data and response_data != {}:
                            sensor_control_map["succesful_server_startup_response"] = 1
                            sensor_control_map["temperature"] = response_data["temperature"]
                            sensor_control_map["humidity"] = response_data["humidity"]
                            sensor_control_map["luminosity"] = response_data["luminosity"]
                            print("Program received:")
                            print(response_data)
                            return
                        else:
                            print("No program active. Trying again to retrieve the startup data from the server.")
                    else:
                        print("Request failed. Trying again to retrieve the startup data from the server.")
            except Exception as e:
                        print("Server down. Trying again to retrieve the startup data from the server.", e)
            await asyncio.sleep(100)
