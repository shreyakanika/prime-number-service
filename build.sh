#!/bin/sh

echo '##########################################'
echo 'Build'
echo '##########################################'
./mvnw clean install -DskipTests
ret=$?
if [ $ret -ne 0 ]; then
  echo "Error while building the application"
  exit $ret
fi

echo '##########################################'
echo 'Run Tests'
echo '##########################################'
./mvnw test
ret=$?
if [ $ret -ne 0 ]; then
  echo "Error while running the tests"
  exit $ret
fi


echo '##########################################'
echo 'Build Package'
echo '##########################################'
./mvnw package -DskipTests
ret=$?
if [ $ret -ne 0 ]; then
  echo "Error while packaging the application"
  exit $ret
fi

echo '##########################################'
echo 'Build Docker Image'
echo '##########################################'
docker build -t prime-number .
ret=$?
if [ $ret -ne 0 ]; then
  echo "Error while building the docker image of the application."
  exit $ret
fi


echo '##########################################'
echo 'Run Prime Number Service'
echo '##########################################'
docker run -d -p 9000:9000 prime-number
ret=$?
if [ $ret -ne 0 ]; then
  echo "Error while running the prime number service."
  exit $ret
fi
