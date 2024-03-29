# XDCCWebloader
Web based Download Manager for xdcc file transfer with ReactJS in the frontend and Spring Data REST in the backend

# Releases
Releases and executables can be found here:
[Releases](https://github.com/MaggiWuerze/xddcwebloader/releases)

# Screenshots

### The Main Download View
![image](https://user-images.githubusercontent.com/9729962/184309789-07bce483-73ec-4159-aaac-4b8e75598cef.png)

### Configuring Bots for different Servers and Channels
<img src="https://user-images.githubusercontent.com/9729962/184310278-6973ae76-a08e-43f4-93d4-0776c8d0a078.png" width="15%"></img> <img src="https://user-images.githubusercontent.com/9729962/184310219-024bc28c-7226-4aa0-bb75-1be2e3efec65.png" width="15%"></img> <img src="https://user-images.githubusercontent.com/9729962/184310113-8230d5ec-4824-4e36-83a0-0ec65a902819.png" width="15%"></img> 

### Settings page
![image](https://user-images.githubusercontent.com/9729962/184346810-f5fb1803-664d-43dc-9b40-1c684ffc290e.png)

# Running The App
### Docker
The best option is to use the provided Docker image or build the container yourself using the ```Dockerfile``` and ```docker-compose.yml``` found in the repository to build and run the app through your docker installation. There are two types of docker image available, which can be found [here](https://github.com/users/MaggiWuerze/packages/container/package/xdccwebloader):
-  The ```latest``` image is based on the current master branch and is build at every push to master.
-  Versions tagged with ```vX.x``` are created when a new versioned release is issued. They are usually more stable and dont change that frequently.
### Locally 
You can also simply execute the jar from the [releases](https://github.com/MaggiWuerze/xddcwebloader/releases) section. it packs everything except a java runtime, so as long as you have java you can run it and start right away.
### From The IDE
This option is mainly meant for people looking into the code or trying to make a PR/Fork for it. Open the project in your favourite Java IDE and execute the XdccloaderApplication class.  

I'd recommend IntelliJ. For this approach also see the building section.

--  

After starting the app (shouldn't take more than a couple of seconds) you can open it in your browser at either [localhost:8080](localhost:8080) or hostname/ip:8080 if you try to access it from another machine. 

# Building
Building the app (and running it from your IDE) requires you to install the following requirements:  
- Node.js (17+)  
- JDK (17+)


# Documentation (soon)
https://maggiwuerze.de/XDCC-Webloader
