from gpiozero import DigitalOutputDevice
from gpiozero import PWMOutputDevice
import digitalio
import board
import adafruit_character_lcd.character_lcd as characterlcd
from time import sleep
from data_maps import *

"""pin control relay"""
pump_relay = DigitalOutputDevice(17)
fan_relay = DigitalOutputDevice(27)
pump_relay.on()
fan_relay.on()

"""pin pwm led"""
pwm_blue = PWMOutputDevice(12, frequency=500)
pwm_red = PWMOutputDevice(13, frequency=500)

"""pin control lcd qapass"""
lcd_columns = 16
lcd_rows = 2

lcd_rs = digitalio.DigitalInOut(board.D5)
lcd_en = digitalio.DigitalInOut(board.D6)
lcd_d4 = digitalio.DigitalInOut(board.D23)
lcd_d5 = digitalio.DigitalInOut(board.D19)
lcd_d6 = digitalio.DigitalInOut(board.D26)
lcd_d7 = digitalio.DigitalInOut(board.D21)

lcd = characterlcd.Character_LCD_Mono(lcd_rs, lcd_en, lcd_d4, lcd_d5, lcd_d6, lcd_d7, lcd_columns, lcd_rows)
lcd.clear()

def exit_handler():
    stop_pump()
    stop_fan()
    pwm_blue.off()
    pwm_red.off()
    lcd.clear()
    
def start_pump():
    pump_relay.off()
    
def stop_pump():
    pump_relay.on()
    
def start_fan():
    fan_relay.off()
    
def stop_fan():
    fan_relay.on()
    
def pump_control_logic():
    if manual_control_map["switch_control"] == 0:
        if sensor_data_map["soilMoisture"] != sensor_control_map["soilMoisture"]:
            start_pump()
        else:
            stop_pump()
    else:
        if manual_control_map["pump"] == 0:
            stop_pump()
        else:
            start_pump()
            
def fan_control_logic():
    if manual_control_map["switch_control"] == 0:
        if sensor_data_map["temperature"] > sensor_control_map["temperature"] or sensor_data_map["humidity"] > sensor_control_map["humidity"]:
            start_fan()
        else:
            stop_fan()
    else:
        if manual_control_map["fan"] == 0:
            stop_fan()
        else:
            start_fan()
        
def led_control_logic():
    if manual_control_map["switch_control"] == 0:
        if sensor_data_map["luminosity"] > sensor_control_map["luminosity"]:
            pwm_blue.value = 0
            pwm_red.value = 0
        else:
            pwm_blue.value = 1 - sensor_data_map["luminosity"] / sensor_control_map["luminosity"]
            pwm_red.value = 1 - sensor_data_map["luminosity"] / sensor_control_map["luminosity"]
    else:
        if manual_control_map["led"] == 0:
            pwm_blue.value = 0
            pwm_red.value = 0
        else:
            pwm_blue.value = 1 - sensor_data_map["luminosity"] / sensor_control_map["luminosity"]
            pwm_red.value = 1 - sensor_data_map["luminosity"] / sensor_control_map["luminosity"]
        
def lcd_control_logic():
    lcd.message = str(round(sensor_data_map["temperature"], 1)) + "C, Hum: " + str(round(sensor_data_map["humidity"], 1)) + "% "
    lcd.cursor_position(0,1)
    lcd.message = "Lum:" + str(round(sensor_data_map["luminosity"] / 5, 1)) + " " + str(sensor_data_map["soilMoisture"])
    
def environment_control_logic():
    if sensor_control_map["succesful_server_startup_response"] == 0:
        stop_pump()
        stop_fan()
        pwm_blue.off()
        pwm_red.off()
        lcd.clear()
    else:
        pump_control_logic()
        fan_control_logic()
        led_control_logic()
        lcd_control_logic()
        
