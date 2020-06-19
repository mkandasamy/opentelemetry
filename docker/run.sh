#!/bin/bash

while getopts ":h:" opt; do
  case $opt in
    h)
      export LOCALHOST_IP=$OPTARG
      echo localhost is $LOCALHOST_IP
      sed "s/LOCALHOST_IP/$LOCALHOST_IP/g" prometheus-template.yml > prometheus.yml
      
      echo Starting servers
      docker-compose -f docker-compose-servers.yml up -d
      echo Waiting 20 secs
      sleep 20
      
      echo Starting data dump
      docker-compose -f docker-compose-db-dump.yml up -d
      echo Waiting 20 secs
      sleep 20
      
      echo Starting services
      docker-compose -f docker-compose-services.yml up -d
      echo Waiting 20 secs
      sleep 20
      
      echo Starting client
      docker-compose -f docker-compose-client.yml up -d
      
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done