<h1 align="center">XDCCWebloader</h1>
<p align="center">A sleek, web-based XDCC download manager—search, queue, and manage downloads from a clean, modern UI.</p>


<hr>

<h2 align="center">Releases</h2>
<p align="center">Releases and executables can be found here:</p>
<p align="center">
  <a href="https://git.maggiwuerze.de/maggiwuerze/xdcwebloader/releases">Releases</a>
</p>

<hr>

<h2 align="center">Screenshots</h2>

<h3 align="center">Overview of all Downloads</h3>

<p align="center"><em>The Main Download View</em></p>
<p align="center">
  <img width="60%"  alt="Download Overview" src="https://github.com/user-attachments/assets/d29280f2-576e-42b1-ad2a-640d7170112c" />
</p>

<h3 align="center">Keep your regular used bots ready to go</h3>

<p align="center"><em>Configuring Bots for different Servers and Channels</em></p>
<p align="center">
  <img width="60%"  alt="Resources Management" src="https://github.com/user-attachments/assets/ec31ec1a-282e-416a-a897-b8876293c0c0" />
</p>

<h3 align="center">Popular Searches directly integrated</h3>

<p align="center"><em>Popular search machines for xdcc are directly integrated and allow seamless starts of Downloads, directly from the Search page</em></p>
<p align="center">
  <img width="60%" alt="Search Page" src="https://github.com/user-attachments/assets/22388101-9716-4efa-8853-4fadac7e4ee2" />
</p>

<hr>

<h2 align="center">Running The App</h2>

<h3 align="center">Docker</h3>
<p align="center">
The best option is to use the provided Docker images (or build the container yourself using the <code>Dockerfiles</code>) and
<code>docker-compose.yml</code> found in the repository to build and run the app through your docker installation. There 
two docker images available, which can be found here: 
</p>

<div align="center">
  <a href="https://hub.docker.com/r/maggiwuerze/xdccwebloader"> Backend </a>
  <br>
  <a href="https://hub.docker.com/r/maggiwuerze/xdccwebloader-frontend"> Frontend </a>
  <br>
</div>

<div align="center">
  <span>- The <code>snapshot</code> image is based on the current master branch and is build at every push to main.</span>
  <br>
  <span>- Versions tagged with <code>vX.x</code> are created when a new versioned release is issued. They are usually more stable and don't change that frequently. The <br><code>latest</code> image is always based on the latest release.</span>
</div>


<h3 align="center">Locally</h3>

<p align="center">
You can also simply execute the jar from the <a href="https://git.maggiwuerze.de/maggiwuerze/xdcwebloader/releases">releases</a> section.
it packs everything except a java runtime, so as long as you have java you can run it and start right away.
</p>

<h3 align="center">From The IDE</h3>

<p align="center">
This option is mainly meant for people looking into the code or trying to make a PR/Fork for it. Open the project in
your favourite Java IDE and execute the XdccloaderApplication class.
</p>
<p align="center">
I'd recommend IntelliJ. For this approach also see the building section.
</p>


<p align="center">
  After starting the app (shouldn't take more than a couple of seconds) you can open it in your browser at <br>
  <a href="http://localhost:8080">localhost:8080</a>
</p>

<hr>

<h2 align="center">Building</h2>

<p align="center">
Building the app (and running it from your IDE) requires you to install the following requirements:
</p>

<div align="center">
    <span>Node.js (24+)</span>
    <br>
    <span>JDK (21+)</span>
</div>

<hr>

<h2 align="center">Documentation (soon)</h2>

<p align="center">
  <a href="https://git.maggiwuerze.de/maggiwuerze/xdcwebloader/wiki">Wiki</a>
</p>
