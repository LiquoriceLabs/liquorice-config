# How to Contribute to Liquorice Config

First and foremost, you have to familiarize yourself with Git & GitHub. Dig through 
[help.github.com](https://help.github.com/), [try.github.io](http://try.github.io/) and the [GitHub cheat sheet](https://github.com/tiimgreen/github-cheat-sheet/blob/master/README.md) if these are new topics for you.

## Contributor Types

### New Contributors

When you're ready to submit your code, please make a [pull request](https://help.github.com/articles/using-pull-requests) against the appropriate branch.

- Always rebase your branch before creating a pull request
- When possible, also squash consecutive related commits
- If the pull request fixes a recorded [issue](https://github.com/LiquoriceLabs/liquorice-config/issues)
  - Please refer to the issue number in the pull request title
  - Please also [reference those issues](https://help.github.com/articles/closing-issues-via-commit-messages) in the commit message
- If your pull request introduces new functionality, please update the [wiki] () accordingly
- Please follow [standard issue etiquette](http://www.defmacro.org/2013/04/03/issue-etiquette.html) when working with issues

#### Core Contributors

You know what you're doing. Just keep doing it.

## Building

1. Install [Gradle](http://www.gradle.org/)
2. Navigate to the project directory and run 'gradle build' from command line

## Best Practices

### Git Essentials

- [Creating good pull requests](http://seesparkbox.com/foundry/creating_good_pull_requests)
- [How to write the perfect pull request](https://github.com/blog/1943-how-to-write-the-perfect-pull-request?utm_content=buffer0eb16&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer)

### Code Standards

We strive to abive by standard Java Code Conventions. Beyond that, there are a few subtle rules that we have for contributing:
 
 1. Handles errors gracefully
 2. Has consistent naming conventions
 3. Compiles without warnings
 4. `final` all immutable variables
   - Although sometimes viewed as a weird habit, it shows intent and can help mitigate subtle bugs 
 5. Only introduces new third party libraries when absolutely necessary
   - This framework is meant to be small and compact
   - There's no sense importing one of the apache commons libraries because you need a single class from it
   - Be prepared to explain your reasoning for adding new dependencies
 6. Compiles without warnings

## Reporting Bugs

 1. Start by searching the GitHub issue tracker for duplicates.
 2. Create a new issue, explaining the problem in detail.
 
## Feature Requests
 
 1. Same system as reporting bugs.
