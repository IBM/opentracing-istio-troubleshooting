#!/bin/bash

kubectl apply -f instrument-craft-shop/deployment/instrument-craft-shop-mp2-notrace.yaml
kubectl apply -f maker-bot/deployment/maker-bot-mp2-notrace.yaml 
kubectl apply -f dbwrapper/deployment/dbwrapper-notrace.yaml
kubectl apply -f pipeline/deployment/notrace

kubectl apply -f instrument-craft-shop/deployment/gateway.yaml
