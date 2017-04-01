# AWS Serverless API to convert math latex to png

To use

   1. Install the AWS command line tools `brew install aws-cli` and `aws configure` with your creds
   1. Create a new bucket to store your deployments `aws s3 mb s3://<yourbucket>`
   1. `export S3_DEPLOY_BUCKET=<yours3 bucket>`
   1. `export AWS_DEFAULT_REGION=<your region>`
   1. `./deploy.sh` will output the URL for your API

## Notes

  * After deploying, add "*/*" to Binary Media Types in "Binary Support" section of API Gateway in the console _(there's probably some way to do this on the command line)_
  * If you get some sort of permissions error, Edit and save "Lambda Function" to same value in
    "Integration Request" section of "Resources" of the "{proxy+}.ANY" section of "Resources in "API Gateway"
