#!/bin/bash

kubectl apply -f instrument-craft-shop/deployment/instrument-craft-shop-mp2.yaml
kubectl apply -f maker-bot/deployment/maker-bot-mp2.yaml 
kubectl apply -f dbwrapper/deployment/dbwrapper.yaml 
kubectl apply -f pipeline/deployment/trace

kubectl apply -f instrument-craft-shop/deployment/gateway.yaml
