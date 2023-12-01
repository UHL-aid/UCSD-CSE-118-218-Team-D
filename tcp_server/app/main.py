from fastapi import FastAPI, WebSocket
from fastapi.responses import HTMLResponse
from dumm_data import generate_data
#import RPi.GPIO as GPIO
import time
import asyncio
import usb.core
import usb.util
from tuning import Tuning

dev = usb.core.find(idVendor=0x2886, idProduct=0x0018)

if dev:
    Mic_tuning = Tuning(dev)
#sensorL = 17
#sensorR = 23
#fake_data = "NONE"

#buzzerL = 22
#buzzerR = 12

#GPIO.setmode(GPIO.BCM)
#GPIO.setup(sensorL, GPIO.IN)
#GPIO.setup(sensorR, GPIO.IN)
#GPIO.setup(buzzerL, GPIO.OUT, initial=GPIO.LOW)
#GPIO.setup(buzzerR, GPIO.OUT, initial=GPIO.LOW)

#def detectL(sensorL):
#    print("LEFT")
#    global fake_data
#    fake_data = "LEFT"

#def detectR(sensorR):
#    print("RIGHT")
#    global fake_data
#    fake_data = "RIGHT"

#GPIO.add_event_detect(sensorL, GPIO.RISING, bouncetime=300)
#GPIO.add_event_callback(sensorL, detectL)
#GPIO.add_event_detect(sensorR, GPIO.RISING, bouncetime=300)
#GPIO.add_event_callback(sensorR, detectR)
        
app = FastAPI()

html = """
<!DOCTYPE html>
<html>
    <head>
        <title>Chat</title>
    </head>
    <body>
        <h1>WebSocket Chat</h1>
        <form action="" onsubmit="sendMessage(event)">
            <input type="text" id="messageText" autocomplete="off"/>
            <button>Send</button>
        </form>
        <ul id='messages'>
        </ul>
        <script>
            var ws = new WebSocket("ws://localhost:8000/ws"); // //bfd9-69-196-47-69.ngrok-free.app/ws");
            ws.onmessage = function(event) {
                var messages = document.getElementById('messages')
                var message = document.createElement('li')
                var content = document.createTextNode(event.data)
                message.appendChild(content)
                messages.appendChild(message)
                ws.send('OK')
            };
            function sendMessage(event) {
                var input = document.getElementById("messageText")
                ws.send(input.value)
                input.value = ''
                event.preventDefault()
            }
        </script>
    </body>
</html>
"""


@app.get("/")
async def get():
    return HTMLResponse(html)


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    try:
        
        
        while True:
            await asyncio.sleep(0.5)
            data = await websocket.receive_text()
            print(data)
            #await websocket.send_text(f"Message text was: {data}")
            #fake_data = generate_data()
            if(Mic_tuning.is_voice()):
                direction = Mic_tuning.direction
                if(direction >= 45 and direction < 135):
                    message = "front"
                elif(direction >= 135 and direction <225):
                    message = "left"
                elif(direction >= 255 and direction < 315):
                    message = "back"
                else:
                    message = "right"
                
                await websocket.send_text(f"{message}")
            else:
                await websocket.send_text("nothing detected")
            print(Mic_tuning.read('AGCGAIN'))
    except WebSocketDisconnect:
        print("client gone")
