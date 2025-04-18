import aiohttp
from aiohttp import web
from request_handlers import *
from actuators import *
import asyncio
from data_maps import *

async def on_startup(raspberry_id):
    await get_startup_info_from_server(raspberry_id) #de la rpi catre server

async def actuator_controller():
    while True:
        environment_control_logic()
        await asyncio.sleep(0)

async def server():
    app = web.Application()
    app.router.add_post('/receive-sensor-data', handle_sensor_data_receive) #de la esp, respectiv de la rpi catre server
    app.router.add_post('/{raspberry_id}/receive-control-command', handle_control_command) #de la server
    app.router.add_post('/{raspberry_id}/receive-custom-program', handle_modify_custom_program) #de la server
    app.router.add_post('/{raspberry_id}/receive-program-stop', handle_program_stop) #de la server
    app.router.add_post('/{raspberry_id}/receive-program-start', handle_program_start) #de la server
    
    print(sensor_control_map)

    runner = web.AppRunner(app)
    await runner.setup()
    site = web.TCPSite(runner, "0.0.0.0", 5000)
    await site.start()
    await asyncio.Event().wait()

async def main():
    await on_startup(RASPBERRY_ID)
    await asyncio.gather(server(), actuator_controller())

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("Shutting down...")
        exit_handler()
