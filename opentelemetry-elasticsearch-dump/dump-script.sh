#!/bin/bash
sleep 5
curl http://192.168.0.3:9200/
ls -alR
elasticdump --input=data/item-mapping.json --output=http://192.168.0.3:9200/item --type=mapping
elasticdump --input=data/item-data-lite.json --output=http://192.168.0.3:9200/item --type=data
elasticdump --input=data/review-mapping.json --output=http://192.168.0.3:9200/review --type=mapping
elasticdump --input=data/review-data-lite.json --output=http://192.168.0.3:9200/review --type=data
elasticdump --input=data/stock-mapping.json --output=http://192.168.0.3:9200/stock --type=mapping
elasticdump --input=data/stock-data-lite.json --output=http://192.168.0.3:9200/stock --type=data