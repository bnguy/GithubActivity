Priscilla Yu
Brittney Nguy

swap1.sh - Will perform the swap from web2 to web1. Looks exactly like swap2,sh, except with the URLs swapped. What it does is replace the web2 URL in nginx.conf with the web1 URL.
doswap.sh - Will automate the swap process. Takes in the parameter of what you want to swap to. Kills any existing containers with the same names. Then based on the parameter given, it will run the respective container, rename it, and perform the swap. It will then close the old image.

Prior to running the doswap, we build both ng and activity (Vincint's old files). We then run dorun.sh, which will build containers for the two images. 