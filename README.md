
# Status Tags [![](http://cf.way2muchnoise.eu/status-tags.svg)](https://www.curseforge.com/minecraft/mc-mods/status-tags) [![](http://cf.way2muchnoise.eu/versions/status-tags.svg)](https://www.curseforge.com/minecraft/mc-mods/status-tags)

To use Status Tags in your projects, include this in your build.gradle:
```
repositories {
	maven {
		url "https://maven.blamejared.com/"
	}
}

dependencies {
	deobfCompile "tschipp.statustags:statustags-MCVERSION:MODVERSION" 
}
```
Make sure to replace `MCVERSION` and `MODVERSION` with the appropriate versions.
