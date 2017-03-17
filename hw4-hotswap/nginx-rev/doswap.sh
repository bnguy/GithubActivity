#/bin/bash

function killitif {
	docker ps -a  > /tmp/yy_xx$$
	if grep --quiet $1 /tmp/yy_xx$$
		then 
		echo "killing older version of $1"
		docker rm -f `docker ps -a | grep $1  | sed -e 's: .*$::'`
	fi
}

killitif $1

if [[ $1 == "activity" ]]; then
	docker run --network=ecs189_default --name=web1 -d activity
	sleep 10 && docker exec ecs189_proxy_1 /bin/bash /bin/swap1.sh
	killitif web2
elif [[ $1 == "activity2" ]]; then
	docker run --network=ecs189_default --name=web2 -d activity2
	docker rename ecs189_web1_1 web1
	sleep 10 && docker exec ecs189_proxy_1 /bin/bash /bin/swap2.sh
	killitif web1
fi
