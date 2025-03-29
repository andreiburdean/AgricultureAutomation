import aiohttp
import request_handlers
from aiohttp import web
from request_handlers import *
from actuators import *


async def actuator_controller():
    while True:
        pump_control_logic()
        fan_control_logic()
        led_control_logic()
        lcd_control_logic()
        await asyncio.sleep(0)

async def server():
    app = web.Application()
    app.router.add_post('/receive-sensor-data', handle_sensor_data_receive)

    runner = web.AppRunner(app)
    await runner.setup()
    site = web.TCPSite(runner, "0.0.0.0", 5000)
    await site.start()
    await asyncio.Event().wait()

async def main():
    await asyncio.gather(server(), actuator_controller())

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("Shutting down...")
        exit_handler()
