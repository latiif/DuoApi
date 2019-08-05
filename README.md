# DuoApi
[![](https://img.shields.io/github/languages/top/llusx/DuoApi.svg)]()
[![](https://img.shields.io/github/last-commit/llusx/DuoApi)]()
[![](https://img.shields.io/maintenance/yes/2019)]()

Unofficial Java API for Duolingo
---

This project was started in 2017. 
Throughout the years, Duolingo changed their API significantly, rendering older versions of the code useless.

One of the new changes is the utilization of [`jwt`](https://jwt.io/) for authentication. Notice that the one currently used in `DuoApi.java` might be expired when you use it.

I'll try to update it regularly. However it seems a `jwt` token can be used to access data accross many users.

## DuolingoProfileInfo
New wrapper dataclass for information extracted from the `DuoApi` class.