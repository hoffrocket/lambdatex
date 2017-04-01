#!/usr/bin/env bash

set -e

mvn package
aws cloudformation package --template-file spec.yml --output-template-file output-spec.yaml --s3-bucket ${S3_DEPLOY_BUCKET}
aws cloudformation deploy --template-file output-spec.yaml --stack-name LambdaTexStack --capabilities CAPABILITY_IAM
aws cloudformation describe-stacks --stack-name LambdaTexStack
rm output-spec.yaml
