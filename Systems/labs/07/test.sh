#!/bin/bash

red='\e[0;31m'
green='\e[0;32m'
nc='\e[0m'     

base=$(dirname $0)


function tobase(){
    cd $(dirname $0)
}

function ctime(){
    date +"%a %b %d %H:%M:%S %Y"
}


function compile_myps(){
    if [ "$(basename $PWD)"  == "myps" ]
    then
	clang -g -Wall    myps.c   -o myps
    else
	cd myps
	clang -g -Wall    myps.c   -o myps
	cd ..
    fi
}

function compile_mypstree(){
    if [ "$(basename $PWD)"  == "mypstree" ]
    then
	clang -g -Wall    mypstree.c   -o mypstree
    else
	cd mypstree
	clang -g -Wall    mypstree.c   -o mypstree
	cd ..
    fi
}

function compile_fg-shell(){
    if [ "$(basename $PWD)"  == "fg-shell" ]
    then
	clang -g -Wall -lreadline    fg-shell.c   -o fg-shell
    else
	cd fg-shell
	clang -g -Wall -lreadline   fg-shell.c   -o fg-shell
	cd ..
    fi
}


function clean_myps(){
    if [ "$basename $PWD)"  == "myps" ]
    then
	rm -f myps
    else
	cd myps
	rm -f myps
	cd ..
    fi
}

function clean_mypstree(){
    if [ "$basename $PWD)"  == "mypstree" ]
    then
	rm -f mypstree
    else
	cd mypstree
	rm -f mypstree
	cd ..
    fi
}

function clean_fg-shell(){
    if [ "$basename $PWD)"  == "fg-shell" ]
    then
	rm -f fg-shell
    else
	cd fg-shell
	rm -f fg-shell
	cd ..
    fi
}


function compile(){
    compile_myps
    compile_mypstree
    compile_fg-shell
}

function clean(){
    clean_myps
    clean_mypstree
    clean_fg-shell
}



function utest( ){

    if [ "$1" == "$2" ]
    then
	echo -ne "$green pass $nc"
    else
	echo -ne "$red FAIL $nc" 
    fi
    echo -e "\t\t $3"
}

function utest_ne( ){

    if [ "$1" != "$2" ]
    then
	echo -ne "$green pass $nc"
    else
	echo -ne "$red FAIL $nc" 
    fi
    echo -e "\t\t $3"
}

function utest_nz( ){
    if [ ! -z "$1" ]
    then
	echo -ne "$green pass $nc" 
    else
	echo -ne "$red FAIL $nc" 
    fi
    echo -e "\t\t $2"
}

function utest_z( ){
    if [ -z "$1" ]
    then
	echo -ne "$green pass $nc" 
    else
	echo -ne "$red FAIL $nc" 
    fi
    echo -e "\t\t $2"
}


function test_makefile(){
    echo "--- TEST MAKEFILE ---"

    clean_myps
    cd myps
    make 1>/dev/null 2>&1
    res=$(ls myps)
    utest_nz "$res" "myps (make)"

    make clean 1>/dev/null 2>&1
    res=$(ls foursons 2>/dev/null)
    utest_z "$res" "myps (make clean)"
    cd ..

    clean_mypstree
    cd mypstree
    make 1>/dev/null 2>&1
    res=$(ls mypstree)
    utest_nz "$res" "mypstree (make)"

    make clean 1>/dev/null 2>&1
    res=$(ls foursons 2>/dev/null)
    utest_z "$res" "mypstree (make clean)"
    cd ..

    clean_fg-shell
    cd fg-shell
    make 1>/dev/null 2>&1
    res=$(ls fg-shell)
    utest_nz "$res" "fg-shell (make)"

    make clean 1>/dev/null 2>&1
    res=$(ls foursons 2>/dev/null)
    utest_z "$res" "fg-shell (make clean)"
    cd ..

    echo
}

function test_myps(){
    echo "--- TEST MYPS ---"

    cd myps    
    compile_myps

    ppid=$$
    #start some tasks
    sleep 100 &
    pid1=$!
    sleep 200 &
    pid2=$!

    cat <<EOF > loop.sh
#!/bin/bash
 while [ 1 ]; do echo -n "";done
EOF
    chmod +x loop.sh
    ./loop.sh &
    pid3=$!

    cmd="./myps $pid1 | tail -1 | tr \"\\\\t\" \" \""
    res=$(eval $cmd)
    expect="$pid1 sleep S $ppid"
    utest "$res" "$expect" "(myps sleep 100)"

    cmd="./myps $pid3 | tail -1 | tr \"\\\\t\" \" \" | sed s/D/R/"
    res=$(eval $cmd)
    expect="$pid3 loop.sh R $ppid"
    utest "$res" "$expect" "(myps loop) $cmd"


    cmd="./myps $pid1 $pid2 | tail -2 | tr \"\\\\n\" \" \" | tr \"\\\\t\" \" \"" 
    res=$(eval $cmd)
    expect="$pid1 sleep S $ppid $pid2 sleep S $ppid "
    utest "$res" "$expect" "(myps 2 pids) $cmd"

    cmd="./myps $pid1 $pid2 $pid3 | tail -3 | tr \"\\\\n\" \" \" | tr \"\\\\t\" \" \"" 
    res=$(eval $cmd)
    expect="$pid1 sleep S $ppid $pid2 sleep S $ppid $pid3 loop.sh R $ppid "
    utest "$res" "$expect" "(myps 3 pids) $cmd"

    cmd="./myps BAD_PID 2>&1| tail -1 "
    res=$(eval $cmd)
    utest_nz "$res" "(myps prints error) $cmd"

    cmd="./myps $pid2 BAD_PID $pid1 2>/dev/null| tail -1 | tr \"\\\\t\" \" \""
    res=$(eval $cmd)
    expect="$pid1 sleep S $ppid"
    utest "$res" "$expect" "(continues through error) $cmd"


    kill $pid1 $pid2 $pid3 
    killall loop.sh

    rm -f loop.sh

    cd ..
    echo
}

function test_mypstree(){
    echo "--- TEST MYPSTREE ---"

    cd mypstree
    compile_mypstree

    base_mypstree="~aviv/lab7-bin/mypstree"

    cat <<EOF > loop.sh
#!/bin/bash
 sleep 300 &
 pid=\$!
 echo \$pid
 while [ 1 ]; do echo -n "";done
EOF
    chmod +x loop.sh
    ./loop.sh > sleep_pid &
    loop_pid=$!
    sleep 0.5
    sleep_pid=$(cat sleep_pid)


    #echo $loop_pid $sleep_pid

    cmd="./mypstree $loop_pid"
    res=$(eval $cmd)
    expect=$(eval "$base_mypstree $loop_pid" )
    utest "$res" "$expect" "(pstree loop) $cmd "

    cmd="./mypstree $sleep_pid | tail -3 | head -2  | sed \"s/^[ ]//g\" | sed s/??????//g | tr \"\\\\n\" \" \"  | tr -s \" \" | sed \"s/^[ ]//g\" | sed \"s/[ ]$//g\""
    res=$(eval $cmd)
    echo "$res"
    expect="loop.sh sleep"
    utest "$res" "$expect" "(pstree sleep) $cmd ($res)"

    kill $loop_pid
    cmd="./mypstree $sleep_pid | tail -3 | head -2 | sed \"s/^[ ]//g\" | sed s/??????//g | tr \"\\\\n\" \" \"  | tr -s \" \" | sed \"s/^[ ]//g\" | sed \"s/[ ]$//g\""
    res=$(eval $cmd)
    echo "$res"
    expect="init sleep"
    utest "$res" "$expect" "(pstree sleep) $cmd ($res)"

    kill $sleep_pid

    rm -f loop.sh sleep_pid
    cd ..

    echo
}

function test_fg-shell(){
    echo "--- TEST FG-SHELL ---"
    
    cd fg-shell
    compile_fg-shell

    python <<EOF >/dev/null
import os,sys,subprocess,fcntl,time,signal

def get_res(fd):
    res = []
    while True:
        try:
            v = os.read(fd.fileno(), 1024)
            res.append(v)
            if len(v) < 1024: break
        except OSError as e:
            print >>sys.stderr, "ERROR", e
            break

    return "".join(res)

if __name__ == "__main__":

    #os.chdir(os.path.dirname(sys.argv[0]))
    #os.chdir("fg-shell")

    #start the fg-shell
    #print os.getcwd()

    fgshell = subprocess.Popen(["./fg-shell"], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    flags = fcntl.fcntl(fgshell.stdout, fcntl.F_GETFL)
    fcntl.fcntl(fgshell.stdout, fcntl.F_SETFL, os.O_NONBLOCK)

    fgshell.stdin.write("touch a\n")
    fgshell.stdin.flush()
    time.sleep(0.1)

    fgshell.stdin.write("ls a\n")
    fgshell.stdin.flush()
    time.sleep(0.1)

    time.sleep(0.1)

    res = get_res(fgshell.stdout)
    
    expect = """fg-shell (-1) #> touch a
fg-shell (-1) #> ls a
a
fg-shell (-1) #> """

    print res, expect

    fgshell.stdin.write("rm a\n");
    fgshell.stdin.flush()
    time.sleep(0.1)

    fgshell.kill()
    if  res.strip() == expect.strip():
        print "SUCCESS"
        exit(0)
    else:
        exit(1)
EOF

    res=$?
    
    utest "$res" "0" "(fg-shell touch in foreground)"
 
    python <<EOF >/dev/null
import os,sys,subprocess,fcntl,time,signal

def get_res(fd):
    res = []
    while True:
        try:
            v = os.read(fd.fileno(), 1024)
            res.append(v)
            if len(v) < 1024: break
        except OSError as e:
            print >>sys.stderr, "ERROR", e
            break

    return "".join(res)

if __name__ == "__main__":

    #os.chdir(os.path.dirname(sys.argv[0]))
    #os.chdir("fg-shell")

    #start the fg-shell
    #print os.getcwd()

    fgshell = subprocess.Popen(["./fg-shell"], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    flags = fcntl.fcntl(fgshell.stdout, fcntl.F_GETFL)
    fcntl.fcntl(fgshell.stdout, fcntl.F_SETFL, os.O_NONBLOCK)

    fgshell.stdin.write("cat\n")
    fgshell.stdin.flush()
    time.sleep(0.1)
    res = get_res(fgshell.stdout)

    c_pid = int(os.popen("pgrep -P %d"%fgshell.pid).read())
    
    os.killpg(c_pid, signal.SIGSTOP)

    c_state = os.popen("ps o state %d | tail -1"%c_pid).read().strip()
    f_state = os.popen("ps o state %d | tail -1"%fgshell.pid).read().strip()
    
    res = get_res(fgshell.stdout)

    expect = "fg-shell (%d) #> "%(c_pid)

    fgshell.kill()
    if c_state == "T" and f_state == "S" and res.strip() == expect.strip():
        print "SUCCESS"
        exit(0)
    else:
        exit(1)
    
EOF

    res=$?
    utest "$res" "0" "(fg-shell cat stopped to background)"

    python <<EOF >/dev/null
import os,sys,subprocess,fcntl,time,signal

def get_res(fd):
    res = []
    while True:
        try:
            v = os.read(fd.fileno(), 1024)
            res.append(v)
            if len(v) < 1024: break
        except OSError as e:
            print >>sys.stderr, "ERROR", e
            break

    return "".join(res)

if __name__ == "__main__":

    #os.chdir(os.path.dirname(sys.argv[0]))
    #os.chdir("fg-shell")

    #start the fg-shell
    #print os.getcwd()

    fgshell = subprocess.Popen(["./fg-shell"], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    flags = fcntl.fcntl(fgshell.stdout, fcntl.F_GETFL)
    fcntl.fcntl(fgshell.stdout, fcntl.F_SETFL, os.O_NONBLOCK)

    fgshell.stdin.write("cat\n")
    fgshell.stdin.flush()
    time.sleep(0.1)

    c_pid = int(os.popen("pgrep -P %d"%fgshell.pid).read())
    
    os.killpg(c_pid, signal.SIGSTOP)
    
    fgshell.stdin.write("fg\n");
    fgshell.stdin.flush()
    time.sleep(0.5)

    
    
    c_state = os.popen("ps o state %d | tail -1"%c_pid).read().strip()
    f_state = os.popen("ps o state %d | tail -1"%fgshell.pid).read().strip()

    os.killpg(c_pid, signal.SIGTERM)

    time.sleep(1)

    res = get_res(fgshell.stdout)

    #print res

    expect = """fg-shell (-1) #> cat

fg-shell (%d) #> fg
fg-shell (-1) #>"""%(c_pid)

    
    fgshell.kill()
    if c_state == "S" and f_state == "S" and tuple(l for l in res.strip().split("\n") if l) == tuple(l for l in expect.strip().split("\n") if l):
        print "SUCCESS"
        exit(0)
    else:
        print "FAIL"
        exit(1)

EOF
    res=$?
    utest "$res" "0" "(fg-shell cat to foreground)"

    python <<EOF >/dev/null
#!/usr/bin/python

import os,sys,subprocess,fcntl,time,signal

def get_res(fd):
    res = []
    while True:
        try:
            v = os.read(fd.fileno(), 1024)
            res.append(v)
            if len(v) < 1024: break
        except OSError as e:
            print >>sys.stderr, "ERROR", e
            break

    return "".join(res)

if __name__ == "__main__":

    #os.chdir(os.path.dirname(sys.argv[0]))
    #os.chdir("fg-shell")

    #start the fg-shell
    #print os.getcwd()

    fgshell = subprocess.Popen(["./fg-shell"], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    flags = fcntl.fcntl(fgshell.stdout, fcntl.F_GETFL)
    fcntl.fcntl(fgshell.stdout, fcntl.F_SETFL, os.O_NONBLOCK)

    fgshell.stdin.write("cat\n")
    fgshell.stdin.flush()
    time.sleep(0.1)

    c_pid = int(os.popen("pgrep -P %d"%fgshell.pid).read())
    
    os.killpg(c_pid, signal.SIGSTOP)
    
    fgshell.stdin.write("cat\n");
    fgshell.stdin.flush()
    time.sleep(0.5)


    c2_pid = int(os.popen("pgrep -P %d|tail -1"%fgshell.pid).read())
    os.killpg(c2_pid, signal.SIGSTOP)

    time.sleep(0.5)

    c_state = os.popen("ps o state %d | tail -1"%c_pid).read().strip()
    c2_state = os.popen("ps o state %d | tail -1"%c2_pid).read().strip()
    f_state = os.popen("ps o state %d | tail -1"%fgshell.pid).read().strip()

    os.killpg(c2_pid, signal.SIGTERM)

    time.sleep(1)

    res = get_res(fgshell.stdout)

    #print >>sys.stderr, c_state,c2_state,f_state
    #print res

    expect = """fg-shell (-1) #> cat

fg-shell (%d) #> cat


fg-shell (%d) #> """%(c_pid,c_pid)

#    print >>sys.stderr, c_state,c2_state,f_state
#    print >>sys.stderr, res.strip().split()
#    print >>sys.stderr, expect.strip().split()

    
    fgshell.kill()
    if c_state == "T" and c2_state == "S" and f_state == "S" and tuple(l for l in res.strip().split("\n") if l) == tuple(l for l in expect.strip().split("\n") if l):
        print "SUCCESS"
        exit(0)
    else:
        print "FAIL"
        exit(1)
EOF
    res=$?
    utest "$res" "0" "(fg-shell no two process in bakground)"

    cd ..
    echo 
}

if [ ! -z $1 ]
then
    cd $1
else
    cd $(dirname $0)
fi

clean
test_makefile

compile

test_myps
test_mypstree
test_fg-shell

