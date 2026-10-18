# mvn-jasper-font-solaimanlipi

[![CI](https://github.com/Oblivion97/mvn-jasper-font-solaimanlipi/actions/workflows/ci.yml/badge.svg)](https://github.com/Oblivion97/mvn-jasper-font-solaimanlipi/actions/workflows/ci.yml)

The **SolaimanLipi** Bangla font, packaged as a JasperReports font extension.

Add it as a Maven dependency and [JasperReports](https://community.jaspersoft.com/) finds the font automatically. You
don't need to install fonts on the server or copy files, and the font is embedded in exported PDFs.

| | |
|---|---|
| Font | SolaimanLipi (regular, bold, italic) |
| Registered family names | `SolaimanLipi` |
| Font license | SIL Open Font License 1.0 |

## Use it

Add the repository and the dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>oblivion-solaimanlipi-github</id>
        <url>https://raw.githubusercontent.com/Oblivion97/mvn-jasper-font-solaimanlipi/main/</url>
        <snapshots><enabled>false</enabled></snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>io.github.oblivion97</groupId>
    <artifactId>jasper-font-solaimanlipi</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then use the family name in your report, for example `fontName="SolaimanLipi"`.

## How it works

The jar contains a `jasperreports_extension.properties` file. At startup, JasperReports scans the classpath for these
files and registers the font families they list. The font files live under `io/github/oblivion97/fonts/…`, so they
don't clash with other font jars. A test renders a real PDF with JasperReports 6.20 and checks that the font is
embedded.

## Credits and license

The font files are distributed **unmodified**, and the jar includes the font's license under `META-INF/`.

- **SolaimanLipi:** designed by Solaiman Karim and Al Mamun Sumon, published by [Ekushey](https://www.ekushey.org).
  Licensed under the [SIL Open Font License 1.0](https://spdx.org/licenses/OFL-1.0.html); the full text is in
  `META-INF/LICENSE-OFL-1.0.txt`.

The build files in this repository (pom, tests, workflows, scripts) are under the [MIT License](LICENSE).

## Releasing

See [RELEASING.md](RELEASING.md).
