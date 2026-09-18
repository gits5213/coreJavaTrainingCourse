# Capstone docs

How to run the skeleton is in the module [README](../README.md).

Architecture direction:

```text
Tests → Business workflows → Page objects / API clients → Application
```

Supporting packages: models, factories, configuration, database, logging, reporting, utilities, test data.

If a newcomer cannot find where a class belongs, update this file. Do not grow `utils/` into `EverythingUtil`.
