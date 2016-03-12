# Introduction

Welcome to the developer guide for Your MOM!

This document serves as a guide for new developers on-board the Your MOM team.
We will start off by introducing the high-level architecture of our project.
Following that, we will zoom into the important API and diagrams of each of the
system component. Finally, this guide will provide the instructions for setting
up the development environment and code testing so that new developers can get
started on extending Your MOM.

Ready? Let's get started!

## Our Product

Your MOM, or Management and Organisation Machine (MOM), is a task tracker aimed
at users who are frequently bombarded with new to-dos, be it an office worker,
an undergraduate, or even a software developer like yourself. These users
mostly spend long hours working on or near a computer, and prefer typing over
mouse or voice commands.

## Our Vision

Often, a busy person may have so many tasks to perform that some of them slip
by his mind. Hence, our vision is to enable Your MOM to alleviate the cognitive
load on a busy user by automating the process of task tracking and scheduling.
The key principles that guide our decisions throughout the design and
implementation process are:

* **Simple**: Your MOM is simple to interact with, in that the user simply has
  to type into the command bar. As such, usage of our software is largely
  keyboard-based.
* **Flexible**: Your MOM supports different types of tasks, such as events with
  start and end times, tasks with deadlines, as well as floating tasks that
  have no specified times. We hope that this covers the wide range of tasks
  that our target users receive on a day-to-day basis.
* **Intuitive**: Your MOM allows users to specify tasks in more than one way.
  The command format to enter a task is mostly dependent only on the
  grammatical structure of English, with little artificial structure.

# Architecture

Your MOM consists of X main components, as seen in Figure 1.

# Important API

**Shared entities:**

* `ExecutionResult`: contains the result of executing commands in the back end
* `Command`: contains the instruction and parameters for a command to be
  executed


**Main components**:

* `DecisionEngine`:
* `TranslationEngine`:
* `Dispatcher`:

# Code Examples

# Setting up & testing

**Development Environment**

Eclipse is the preferred development environment. Download and install
the latest Eclipse from https://eclipse.org/downloads/.

**Source Code**

Clone the latest source code from the `develop` branch of the GitHub repo,
located at https://github.com/cs2103jan2016-f13-4j/main. If you want to clone
the latest stable release, checkout the `master` branch of the repository.

**Testing**

All unit tests are available under the `test` directory. You can right click
**test** folder, choose **Run As > JUnit Test** to run all tests, or apply
the same steps to the individual test sources.


# Known issues & future work
