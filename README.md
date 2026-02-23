# XDCCWebloader

Web based Download Manager for xdcc file transfer

# Releases

Releases and executables can be found here:
[Releases](https://github.com/MaggiWuerze/xddcwebloader/releases)

# Screenshots

### The Main Download View

<img width="1652" height="1285" alt="Download Overview" src="https://github.com/user-attachments/assets/d29280f2-576e-42b1-ad2a-640d7170112c" />

### Configuring Bots for different Servers and Channels

<img width="1652" height="1285" alt="Resources Management" src="https://github.com/user-attachments/assets/ec31ec1a-282e-416a-a897-b8876293c0c0" />

### Popular Searches directly integrated

Popular search machines for xdcc are directly integrated and allow seamless starts of Downloads, directly from the Search page

<img width="1652" height="1285" alt="Search Page" src="https://github.com/user-attachments/assets/22388101-9716-4efa-8853-4fadac7e4ee2" />

# Running The App

### Docker

The best option is to use the provided Docker image or build the container yourself using the ```Dockerfile``` and
```docker-compose.yml``` found in the repository to build and run the app through your docker installation. There are
two types of docker image available, which can be
found [here](https://github.com/users/MaggiWuerze/packages/container/package/xdccwebloader):

- The ```latest``` image is based on the current master branch and is build at every push to master.
- Versions tagged with ```vX.x``` are created when a new versioned release is issued. They are usually more stable and
  dont change that frequently.

### Locally

You can also simply execute the jar from the [releases](https://github.com/MaggiWuerze/xddcwebloader/releases) section.
it packs everything except a java runtime, so as long as you have java you can run it and start right away.

### From The IDE

This option is mainly meant for people looking into the code or trying to make a PR/Fork for it. Open the project in
your favourite Java IDE and execute the XdccloaderApplication class.

I'd recommend IntelliJ. For this approach also see the building section.

--

After starting the app (shouldn't take more than a couple of seconds) you can open it in your browser at
either [localhost:8080](localhost:8080) or hostname/ip:8080 if you try to access it from another machine.

# Building

Building the app (and running it from your IDE) requires you to install the following requirements:

- Node.js (17+)
- JDK (17+)

# Documentation (soon)

https://maggiwuerze.de/XDCC-Webloader
