# -*- coding:utf-8 -*-
import os
import re


def kill_process(port):
    try:
        ret = os.popen("lsof -i:" + str(port))
        str_list = ret.read()
        print(str_list)
        ret_list = re.split('', str_list)
        process_pid = list(ret_list[0].split())[-1]
        os.popen('taskkill /pid ' + str(process_pid) + ' /F')
    except:
        pass

win_random = {
    "agent": 0,
    "opponent": 0
}

win_lookt_9 = {
    "agent": 0,
    "opponent": 0
}


port_default = 56000



for i in range(30):
    port = port_default + i

    cmd = f"src/servt -p {port} & src/randt -p {port} & java AgentSubstitute -p {port}"

    val = os.popen(cmd)
    content = val.read()
    content = content.split('\n')


    for line in content:
        player = re.findall(r"Player(.*)", line)
        if len(player) > 0:
            if "wins" in player[0]:
                if player[0][1].lower() == 'o':
                    if agent == 'o':
                        win_random["agent"] += 1
                    else:
                        win_random["opponent"] += 1
                elif player[0][1].lower() == 'x':
                    if agent == 'x':
                        win_random["agent"] += 1
                    else:
                        win_random["opponent"] += 1
            else:
                agent = player[0][-1]
                opponent = 'x' if agent == 'o' else 'o'
    kill_process(port)

# print(win)
port_default += 30

for i in range(30):
    port = port_default + i

    cmd = f"src/servt -p {port} & src/lookt.mac -p {port} & java AgentSubstitute -p {port}"

    val = os.popen(cmd)
    content = val.read()
    content = content.split('\n')


    for line in content:
        player = re.findall(r"Player(.*)", line)
        if len(player) > 0:
            if "wins" in player[0]:
                if player[0][1].lower() == 'o':
                    if agent == 'o':
                        win_lookt_9["agent"] += 1
                    else:
                        win_lookt_9["opponent"] += 1
                elif player[0][1].lower() == 'x':
                    if agent == 'x':
                        win_lookt_9["agent"] += 1
                    else:
                        win_lookt_9["opponent"] += 1
            else:
                agent = player[0][-1]
                opponent = 'x' if agent == 'o' else 'o'
    kill_process(port)

with open("battle.txt", 'a+') as f:
    f.write("Against random\n")
    f.write(f"agent win: {win_random['agent']}, opponent win: {win_random['opponent']}\n")
    f.write('winning rate: {:.2%}\n'.format(win_random["agent"]/(win_random["opponent"] + win_random["agent"])))
    f.write("-----------------\n")

    f.write("Against looket\n")
    f.write(f"agent win: {win_lookt_9['agent']}, opponent win: {win_lookt_9['opponent']}\n")
    f.write('winning rate: {:.2%}\n'.format(win_lookt_9["agent"] / (win_lookt_9["opponent"] + win_lookt_9["agent"])))
    f.write("-----------------\n")

