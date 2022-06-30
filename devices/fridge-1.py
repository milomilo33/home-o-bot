from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import padding
import numpy as np
import time
import base64

with open("fridge-1.key", "rb") as key_file:
    private_key = serialization.load_pem_private_key(
        key_file.read(),
        password=None,
    )
    # message = b"A message I want to sign"
    # signature = private_key.sign(
    #     message,
    #     padding.PSS(
    #         mgf=padding.MGF1(hashes.SHA256()),
    #         salt_length=padding.PSS.MAX_LENGTH
    #     ),
    #     hashes.SHA256()
    # )
    # potpis staviti u novi red ispod poruke
    # print(urllib.parse.quote_plus(signature))
    # public_key = private_key.public_key()
    # public_key.verify(
    #     signature,
    #     message,
    #     padding.PSS(
    #         mgf=padding.MGF1(hashes.SHA256()),
    #         salt_length=padding.PSS.MAX_LENGTH
    #     ),
    #     hashes.SHA256()
    # )

normal_messages = ["Temperature is 0°C|Normal", "Temperature is 1°C|Normal", "Temperature is 2°C|Normal",
                   "Temperature is 3°C|Normal", "Temperature is 4°C|Normal", "Temperature is 5°C|Normal"]
danger_messages = ["Temperature is exceedingly high!|DANGER", "Non-empty fridge turned off|DANGER"]

while True:
    messages = np.random.choice(
      [normal_messages, danger_messages],
      p=[0.9, 0.1]
    )
    message = np.random.choice(messages)

    signature = private_key.sign(
        message.encode('utf-8'),
        padding.PSS(
            mgf=padding.MGF1(hashes.SHA256()),
            salt_length=20
        ),
        hashes.SHA256()
    )
    signature = base64.b64encode(signature)

    with open('fridge-1.txt', 'a', encoding='utf8') as the_file:
        the_file.write(message + '\n')
        the_file.write(signature.decode() + '\n')

    time.sleep(3)
