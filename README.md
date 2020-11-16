# ArticleToAnki
Create Anki decks from news feeds.


For this to work you need the desktop version of [Anki](https://apps.ankiweb.net/) with [AnkiConnect](https://github.com/FooSoft/anki-connect). Once your deck is created, you can synchronize the deck onto your smartphone using your [AnkiWeb](https://ankiweb.net/about) account.

These things are hard-coded right now:
* Feeds are fetched from the [Nieuws feed of MetroNieuws](https://www.metronieuws.nl/in-het-nieuws/feed/)
* This assumes you are making English cards from a Dutch source
* This assume you want the front and back Anki model
* This uses http://localhost:8765 for the AnkiConnect binding

To run this script, you must create a local.properties file in the root of the project, like so:

```
GOOGLE_API_KEY=C:\\path\\to\\key.json
```

Which points to the Google Api key file, as described in the [the Google Cloud API docs](https://cloud.google.com/storage/docs/reference/libraries#setting_up_authentication). You can skip the section on configuring your environment variables, you just need the key JSON file. For the API to add, you want the Google Cloud Translate API.
