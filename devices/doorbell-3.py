from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import padding
import numpy as np
import time
import base64

with open("doorbell-3.key", "rb") as key_file:
    private_key = serialization.load_pem_private_key(
        key_file.read(),
        password=None,
    )

normal_messages = ["Doorbell rung|Normal", "Doorbell sound changed|Normal"]
danger_messages = ["Doorbell rung too many times|DANGER", "Doorbell broken|DANGER"]

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
            # salt_length=padding.PSS.MAX_LENGTH
            salt_length=20
        ),
        hashes.SHA256()
    )
    signature = base64.b64encode(signature)

    with open('doorbell-3.txt', 'a', encoding='utf8') as the_file:
        the_file.write(message + '\n')
        the_file.write(signature.decode() + '\n')

    time.sleep(3)
