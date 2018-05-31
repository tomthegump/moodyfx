MoodyFx
-------

Content of survey.json
-> id: The id of the survey. Used to distinguish surveys done on the same device.
-> question: The question you want to ask in the survey.
-> answerType: The answer possibilities shown to the user. Possible values are RATING, YES_NO_MEEH, YES_NO.

Content of config.json
-> iconSet: The icon set that shall be used. Shall be the name of a folder inside the "icons" folder that contains 5 png files named 0.png, 1.png, ..., 4.png.
-> site: The site where this survey is executed. Can be used to distinguish the same survey at different sites.
-> thanksDisplayDuration: The duration in seconds for displaying the "Thank You" screen to the user.

Exporting a survey to json
-> press Ctrl+E on the rating screen to create an export.json file in the application folder. 