# DuoApi
[![](https://img.shields.io/github/languages/top/llusx/DuoApi.svg)]()
[![](https://img.shields.io/github/last-commit/llusx/DuoApi)]()
[![](https://img.shields.io/maintenance/yes/2019)]()

Unofficial Java API for Duolingo
---

This project was started in 2017. 
Throughout the years, Duolingo changed their API significantly, rendering older versions of the code useless.

One of the new changes is the utilization of [`jwt`](https://jwt.io/) for authentication.

One other thing, is the introduction of a new endpoint for signing in. Work is currently focused on making sure all previous functionality is still compatible with the new updates to the api.

## DuolingoProfileInfo
New wrapper dataclass for information extracted from the `DuoApi` class.