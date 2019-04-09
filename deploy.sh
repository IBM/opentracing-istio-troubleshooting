#!/bin/bash

kubectl apply -f instrument-craft-shop/deployment/instrument-craft-shop-mp2-zipdirect.yaml
kubectl apply -f maker-bot/deployment/maker-bot-mp2-zipdirect.yaml 
kubectl apply -f dbwrapper/deployment/dbwrapper-zipdirect.yaml 
kubectl apply -f pipeline/deployment/zipdirect
kubectl apply -f js_pipeline/deployment/pipeline-js.yaml

kubectl apply -f instrument-craft-shop/deployment/gateway.yaml
