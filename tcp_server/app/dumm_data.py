import random
random.seed()
directions = ["LEFT", "RIGHT", "BACK"]
def generate_data():
    return directions[random.randint(0,2)]