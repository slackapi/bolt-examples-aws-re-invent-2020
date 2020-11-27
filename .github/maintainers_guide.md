# Maintainer's Guide

This document describes tools, tasks and workflow that one needs to be familiar with in order to effectively maintain
this project. If you use this package within your own software as is but don't plan on modifying it, this guide is
**not** for you.

## Tools

This project depends on the Bolt framework and we recommend reviewing the maintainer's guide for the specific language:

- [Bolt for Java - Maintainer's Guide](https://github.com/slackapi/java-slack-sdk/blob/main/.github/maintainers_guide.md)
- [Bolt for JavaScript - Maintainer's Guide](https://github.com/slackapi/bolt-js/blob/main/.github/maintainers_guide.md)
- [Bolt for Python - Maintainer's Guide](https://github.com/slackapi/bolt-python/blob/main/.github/maintainers_guide.md)

## Tasks

### Testing

This package does not have unit tests but has implemented the command `npm test` or similar.

If you'd like to add tests, please review our [contributing guide](./contributing.md) to learn how
to open a discussion and pull request. We'd love to talk through setting up tests!

## Workflow

### Fork

As a maintainer, the development you do will be almost entirely off of your forked version of this package. The exception to this rule pertains to multiple collaborators working on the same feature, which is detailed in the **Branches** section below.

### Branches

`main` is where active development occurs.

When developing, branches should be created off of your fork and not directly off of this package. If working on a long-running feature and in collaboration with others, a corresponding branch of the same name is permitted. This makes collaboration on a single branch possible, as contributors working on the same feature cannot push commits to others' open Pull Requests.

After a major version increment, there also may be maintenance branches created specifically for supporting older major versions.

### Issue Management

Labels are used to run issues through an organized workflow. Here are the basic definitions:

*  `bug`: A confirmed bug report. A bug is considered confirmed when reproduction steps have been
   documented and the issue has been reproduced.
*  `enhancement`: A feature request for something this package might not already do.
*  `docs`: An issue that is purely about documentation work.
*  `tests`: An issue that is purely about testing work.
*  `needs feedback`: An issue that may have claimed to be a bug but was not reproducible, or was otherwise missing some information.
*  `discussion`: An issue that is purely meant to hold a discussion. Typically the maintainers are looking for feedback in this issues.
*  `question`: An issue that is like a support request because the user's usage was not correct.
*  `security`: An issue that has special consideration for security reasons.
*  `good first contribution`: An issue that has a well-defined relatively-small scope, with clear expectations. It helps when the testing approach is also known.
*  `duplicate`: An issue that is functionally the same as another issue. Apply this only if you've linked the other issue by number.

**Triage** is the process of taking new issues that aren't yet "seen" and marking them with a basic
level of information with labels. An issue should have **one** of the following labels applied:
`bug`, `enhancement`, `question`, `needs feedback`, `docs`, `tests`, or `discussion`.

Issues are closed when a resolution has been reached. If for any reason a closed issue seems
relevant once again, reopening is great and better than creating a duplicate issue.


## Everything else

When in doubt, find the other maintainers and ask.
