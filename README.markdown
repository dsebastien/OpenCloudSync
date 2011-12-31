# About
In a few words, the OpenCloudSync project aims to provide a data synchronization solution supporting various data stores (e.g., Dropbox, Box.net, NAS, Network shares, FTP, etc) and different devices (e.g., Linux, Mac OS, Windows, Android, ...). The goal being to free consumers from depending on a specific storage solution.

Many cloud storage solutions have appeared on the market and while one could decide to use a single solution, the problem is that these services can suffer outages or disappear completely and as such, you might instead want to rely on multiple solutions to guarantee that your data remains available.

Also, storing sensible data online is always problematic because once the data is out of your LAN you lose control over it, which means that you need to trust the provider to take care of your data and protect it. But as a client you have no idea how your data is handled, by whom and who can have access to it directly or indirectly (e.g., Patriot act for US companies). There also security risks involved: if the service's security is compromised, then your data is at risk. For those reasons you're probably better off encrypting your data in order to ensure that your privacy is protected unless you're storing stuff you don't care about at all.

Each of these online services offer their own tools to upload/download/sync your data between different devices and their servers. Usually, these tools have to be installed on your devices; that is if your operating system/device is supported and if not, then too bad, you won't easily have access to your data.

Now, suppose that you don't want to use a single service (single point of failure) but that instead, you would like to use two or more different services to store your data online. To some extent, you could manage to force the dedicated tools to do what you want (e.g., use the same folders), but this might not work for all services.

You might not want to share the whole folder to all services; instead, you might want to share folder X with service A and folder X and Y with service B. And maybe sub-folder Z should only be synchronized with an FTP..

The possibilities are numerous but the current solutions provide you with very limited flexibility. Indeed that's normal since the official tools are only there to support their respective service and data portability isn't necessary/useful to them. Fortunately, many of these services are quite open to the Web; APIs & SDKs are usually available to let us build upon the offered services since that's benefitial to them if a whole ecosystem is built around their service.

As a consumer your needs might be different and solutions should exist to allow you to achieve what you want. 

# Status
This project is in a very early stage, we're just thinking about the requirements and high level architecture.
Interested in participating? Check out the development page on the wiki: https://github.com/dsebastien/OpenCloudSync/wiki/Development

Don't hesitate to join us and share your ideas!

# Documentation
Check our the following pages for more information about the project:

* wiki: https://github.com/dsebastien/OpenCloudSync/wiki
* requirements: https://github.com/dsebastien/OpenCloudSync/wiki/Requirements
* development: https://github.com/dsebastien/OpenCloudSync/wiki/Development

# Authors
Sebastien Dubois

* http://twitter.com/dSebastien
* http://github.com/dSebastien
* http://www.dsebastien.net

# License
This project and all associated source code is licensed under the terms of the [GNU General Public License v3 or any later version](http://www.gnu.org/licenses/gpl-3.0.html).