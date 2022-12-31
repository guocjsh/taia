#!/bin/bash


# Help info function
help(){
  echo "--------------------------------------------------------------------------"
  echo ""
  echo "usage: ./taia.sh [install | doc | pack]"
  echo ""
  echo "-install    Install taia to your local Maven repository."
  echo "-doc        Generate Java doc api for taia, you can see it in target dir"
  echo "-pack       Make jar package by Maven"
  echo ""
  echo "--------------------------------------------------------------------------"
}


# Start
sh ./bin/logo.sh
case "$1" in
  'install')
   sh bin/install.sh
	;;
  'doc')
   sh bin/javadoc.sh
	;;
  'pack')
   sh bin/backage.sh
	;;
  'deploy')
   sh bin/deploy.sh
	;;
  *)
    help
esac
