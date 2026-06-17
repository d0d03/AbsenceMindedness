# AbsenceMindedness
> Mozak na pašu — a simple yearly absence tracker for planning and visualizing annual leave.

## Features
- Interactive yearly calendar grid with color coded day statuses
- Supported statuses: used, approved, planned, non-working, paternity leave, home office, blood donation day
- Year navigation
- Persistent storage with encrypted local database
- Data survives app restarts and updates

## Installation
Download the latest installer from the [Releases](../../releases) page and run it. No Java installation required.

## Development Setup
Prerequisites:
- JDK 21+
- Maven 3.9+
- Inno Setup 6 (for building the installer)

Steps:
1. Clone the repository
2. Copy `src/main/resources/application.properties.template` to `src/main/resources/application.properties`
3. Set your own passwords for `db.file.password` and `db.user.password`
4. Run `AbsenceMindednessApplication.java`

## Building the Installer
1. Run `package.bat` to produce the app image
2. Open `installer.iss` in Inno Setup and press `Ctrl+F9` to compile
3. Find the installer in `Output/`

## Tech Stack
- Java 21
- Swing
- H2 Database
- Maven
- Inno Setup
- 
## License
This project is licensed under the GNU General Public License v3.0 — see the [LICENSE](LICENSE) file for details.