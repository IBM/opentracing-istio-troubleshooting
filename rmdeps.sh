#!/bin/bash

kubectl delete deployment instrument-craft-shop
kubectl delete deployment maker-bot
kubectl delete deployment dbwrapper
kubectl delete deployment pipeline-n1
kubectl delete deployment pipeline-n2
kubectl delete deployment pipeline-n3
kubectl delete deployment pipeline-js
kubectl delete -f instrument-craft-shop/deployment/gateway.yaml


