import aiohttp
from aiohttp import web
from request_handlers import *
from actuators import *
import asyncio
from data_maps import *

#asks for the necessary startup information from the application server
async def on_startup(raspberry_id):
    await get_startup_info_from_server(raspberry_id)


#asyncrounously executes the function responsible for controlling the actuators
async def actuator_controller():
    while True:
        environment_control_logic()
        await asyncio.sleep(0)

#runs the controller's server responsible for communicating with the application server
async def server():
    app = web.Application()

    #receives the sensor data from the ESP8266 sensor hub
    app.router.add_post('/receive-sensor-data', handle_sensor_data_receive)

    #receives the manual control command from the application server
    app.router.add_post('/{raspberry_id}/receive-control-command', handle_control_command)

    #receives the new custom program parameters from the application server, after they have been modified in the mobile application
    app.router.add_post('/{raspberry_id}/receive-new-custom-program-parameters', handle_modify_custom_program_parameters)

    #receives, from the application server, the command responsible for stopping the current running program, after it has been manually stopped in the mobile application
    app.router.add_post('/{raspberry_id}/receive-program-stop', handle_program_stop)

    #receives, from the application server, the command responsible for starting the current running program, after it has been manually started in the mobile application
    app.router.add_post('/{raspberry_id}/receive-program-start', handle_program_start)

    #sets up the server
    runner = web.AppRunner(app)
    await runner.setup()
    site = web.TCPSite(runner, "0.0.0.0", 5000)
    await site.start()
    await asyncio.Event().wait()

#creates the main function that puts together the startup function, actuator controller function and the server function
async def main():
    await on_startup(RASPBERRY_ID)
    await asyncio.gather(server(), actuator_controller())

#runs the main function
if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("Shutting down...")
        exit_handler()
