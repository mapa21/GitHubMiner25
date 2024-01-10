# GitHubMiner

GitHubMiner is a project that we developed for the Software Design course at the Vrije Universiteit Amsterdam.

## Project Description
This project consists in the implementation of a system for mining relevant metrics from an already-existing GitHub repository. The user of this game is a software developer who wants to get some information from the repository. Extracted information can include: a ranking of the contributors based on the number of their commits, the contributors who worked more in the weekends (poor them), the commits with the highest churn (e.g., the highest number of changed lines of code), the contributors who are in the project for the longest amount of time, newcomers in the project, etc. You can be creative with respect to the extracted metrics! The system can have either (i) a (very) basic user interface or (ii) a command-line interface, it is up to you (and this will not influence your final grade). The usage scenario of your system is: (1) the user provides a URL of the GitHub repository they want to analyze, (2) the system locally clones the repository and informs the user about the progress of the cloning and shows some very basic information about the cloned repository, (3) the system provides to the user a set of predefined actions that can be performed on the repository (e.g., “identify the most productive contributors”, “identify who is committing the most on the readme.md file”, “Show who is working in the weekends”, etc.), (4) the user can decide to remove the cloned repo from his machine and start again with another GitHub repo (bringing them back to step 1). Internally the system uses exclusively the git log command for extracting the information about the repository under analysis (in other words, it is NOT possible to use libraries like JGit). The main technical challenge of this project is that the system must be as independent as possible from the specific metrics (and their corresponding predefined actions); in other words, it should be easy to add/configure new metrics and actions with minimal changes in the models/code. Ideas for a bonus: (1) generate a printable report that visualizes and summarizes in a nice way the computed metrics; (2) integrate additional information coming from GitHub that is not available on a locally-cloned repository (e.g., pull requests, issues, comments).

## Development Team

**Team name**: Pirates

### Members
* Maria Paula **Jimenez Moreno**
* Lennart **Schulz**
* Laura **Stampf**
* Dovydas **Vadišius**


(⌐■_■)

## Dependencies
**Java Runtime**: v11.0.x.x

## Usage
To run our application, use `make run`

## Application Notes
* Cancelling the input via interrupt signal (control + c) without quitting the application is only possible on macOS. Here, the interrupt can even be used to exit repos or log out of an account.