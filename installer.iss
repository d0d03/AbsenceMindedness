#define MyAppName "AbsenceMindedness"
#ifndef MyAppVersion
    #define MyAppVersion "0.0.0"
#endif
#define MyAppPublisher "Your Name"
#define MyAppExeName "AbsenceMindedness.exe"
#define MyAppSourceDir "dist\AbsenceMindedness"
#define MyAppDataDir "{userappdata}\AbsenceMindedness"

[Setup]
AppId={{B3757211-4EAC-4CDB-88EB-2ACD499EB6AC}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
LicenseFile=LICENSE
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
UninstallDisplayIcon={app}\{#MyAppExeName}
OutputDir=Output
OutputBaseFilename=AbsenceMindedness-{#MyAppVersion}-Setup
Compression=lzma
SolidCompression=yes
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "Create a desktop shortcut"; GroupDescription: "Additional icons:"; Flags: unchecked

[Files]
Source: "{#MyAppSourceDir}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "Launch {#MyAppName}"; Flags: nowait postinstall skipifsilent

[Code]
var
  BackedUpFilePassword: String;
  BackedUpUserPassword: String;
  HadExistingInstall: Boolean;

function ExtractPasswordValue(Lines: TArrayOfString; Key: String): String;
var
  I: Integer;
begin
  Result := '';
  for I := 0 to GetArrayLength(Lines) - 1 do
  begin
    if Pos(Key, Lines[I]) = 1 then
    begin
      Result := Copy(Lines[I], Length(Key) + 1, MaxInt);
      exit;
    end;
  end;
end;

function GeneratePassword(Len: Integer): String;
var
  Chars: String;
  I: Integer;
begin
  Chars := 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  Result := '';
  for I := 1 to Len do
    Result := Result + Chars[Random(Length(Chars)) + 1];
end;

procedure WriteAppProperties(FilePassword: String; UserPassword: String);
var
  PropsFile: String;
  Lines: TArrayOfString;
  AppData: String;
begin
  PropsFile := ExpandConstant('{app}\app\classes\application.properties');
  AppData := ExpandConstant('{userappdata}');
  StringChange(AppData, '\', '/');
  SetArrayLength(Lines, 6);
  Lines[0] := 'db.url=jdbc:h2:'+ AppData+ '/AbsenceMindedness/absencemindedness;CIPHER=AES;TRACE_LEVEL_FILE=0;AUTO_SERVER=FALSE';
  Lines[1] := 'db.user=sa';
  Lines[2] := 'db.file.password=' + FilePassword;
  Lines[3] := 'db.user.password=' + UserPassword;
  Lines[4] := 'app.version={#MyAppVersion}';
  Lines[5] := 'app.locale=hr';
  SaveStringsToFile(PropsFile, Lines, False);
end;

procedure CurStepChanged(CurStep: TSetupStep);
var
  PropsFile:String;
  ExistingLines: TArrayOfString;
  DataDir: String;
begin
  //BEFORE files are copied - backup existing passwords if this is an update
  if CurStep = ssInstall then
  begin
    PropsFile := ExpandConstant('{app}\app\classes\application.properties');
    HadExistingInstall := FileExists(PropsFile)
    if HadExistingInstall then
    begin
      LoadStringsFromFile(PropsFile, ExistingLines);
      BackedUpFilePassword := ExtractPasswordValue(ExistingLines, 'db.file.password=')
      BackedUpUserPassword := ExtractPasswordValue(ExistingLines, 'db.user.password=')
    end;
  end;

  //AFTER files are copied - restore old passwords or generate new ones
  if CurStep = ssPostInstall then
  begin
    if HadExistingInstall and (BackedUpFilePassword <> '') and (BackedUpUserPassword <> '') then
    begin
      //Update - restore the old passwords so existing DB still works
      WriteAppProperties(BackedUpFilePassword, BackedUpUserPassword);
    end
    else
    begin
      //Fresh install - generate new passwords
      WriteAppProperties(GeneratePassword(32), GeneratePassword(32));
    end;

    // Create user data directory
    DataDir := ExpandConstant('{userappdata}\AbsenceMindedness');
    if not DirExists(DataDir) then
      CreateDir(DataDir);
  end;
end;

procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
var
  DataDir: String;
  MsgResult: Integer;
begin
  if CurUninstallStep = usUninstall then
  begin
    DataDir := ExpandConstant('{userappdata}\AbsenceMindedness');
    if DirExists(DataDir) then
    begin
      MsgResult := MsgBox(
        'Do you want to delete your calendar data?' + #13#10 +
        'This will permanently delete all your absence records.',
        mbConfirmation,
        MB_YESNO
      );
      if MsgResult = IDYES then
        DelTree(DataDir, True, True, True);
    end;
  end;
end;