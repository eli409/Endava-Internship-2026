# Retro — TAP26 homework tasks 1–7 (2026-10-22)

| Field | Value |
|---|---|
| Scope | Homework tasks 1–7: local setup, API/UI/E2E testing, accessibility proposal, tablet viewport |
| Participants | Student and GitHub Copilot Chat |
| Evidence | Commits `d1752f0`, `8ed01da`, `b64cd5d`, `1e5d50d`, `a4d91a9`, `3cf1a3e`, `8d20f36`, `56134fb`; `BUG-001-unknown-contact-delete.md`; `a11y-20261022-notifications.md`; `impact-20261022-ui-tablet-500x800.md` |

## Context
The goal was to set up the Real World App on Windows, learn the Playwright suite, and complete seven homework exercises on separate local Git branches. The work covered known API/UI issues, a mocked empty state, a two-session payment journey, an A11Y proposal, and a 500×800 tablet project.

## Timeline
| Time | Event | Evidence |
|---|---|---|
| 2026-10-20–21 | Installed dependencies, Chromium and Git; pinned Node 22.20.0; created local baseline | `d1752f0`; doctor output: 0 errors |
| 2026-10-21 | Added expected-failure regression tests for KI-05 and KI-01 | `8ed01da`, `b64cd5d`; both focused tests repeated twice |
| 2026-10-21–22 | Added KI-31 UI test and A11Y-02 proposal | `1e5d50d`, `a4d91a9` |
| 2026-10-22 | Added empty bank-account test and isolated two-context Alice/Bob journey | `3cf1a3e`, `8d20f36`; targeted tests passed twice |
| 2026-10-22 | Added 500×800 tablet project and recorded G-14 findings | `56134fb`; 55 passed, 19 failed, 1 skipped; typecheck passed |

## Metrics
| Metric | Value |
|---|---|
| Tests added / updated | 5 / 0 |
| Test runs green / red | API baseline: 73 passed; focused desktop/mobile/E2E checks passed twice; known-bug tests were expected failures; tablet suite: 55 passed, 19 failed, 1 skipped |
| Quality gates passed / failed (which) | Node 22.20.0, dependency/browser checks and Playwright typechecks passed; tablet run exposed 19 failures for investigation/reporting |
| Bugs filed | `BUG-001` linked to KI-01; regression tests also cover KI-05 and KI-31 |
| Review verdicts | No independent reviewer verdict recorded |

## What went well
- The local baseline and one-branch-per-task workflow kept the homework commits isolated and reviewable.
- Repeating focused Playwright tests twice made the expected failures and passing checks easier to distinguish.
- Separate browser contexts verified Alice and Bob had independent sessions.
- The tablet run exposed a concrete breakpoint mismatch instead of hiding it by weakening assertions.

## What didn't go well
- Initial setup instructions did not make the nested ZIP folder and PowerShell `.ps1` versus `.cmd` behavior obvious; several installs were attempted from the parent folder.
- Git's first staged set included `.env`; adding an ignore rule and verifying the index prevented it from entering the baseline commit.
- Git ignored most generated reports, and a report was briefly committed empty before being amended with its content.
- The first tablet suite run had 19 failures, including repeated navigation timeouts and unrelated accessibility/cookie assertions.

## Surprises
- The environment was fully usable from PowerShell once the correct project folder, Node version, and `.cmd` launchers were used.
- At 500px the app uses its mobile layout (600px breakpoint), while the test helper considers only widths below 414px mobile.
- Expected-failure tests can show failed assertions in the report while Playwright still summarizes the run as passed.

## Root cause of the biggest problem (5 whys)
1. Why did setup commands fail? PowerShell selected blocked `.ps1` wrappers, and commands were sometimes run from the outer ZIP folder.
2. Why did that happen? The instructions gave generic `yarn`/`npm` commands without a PowerShell-specific `.cmd` path or a clear root-folder check.
3. Why were the commands ambiguous? The checkout was a ZIP with two nested `RWA` folders, and terminal examples assumed a single repository root.
4. Why was the setup guide insufficient? It did not explicitly verify the expected `yarn.lock` and `playwright/package-lock.json` before installing.
5. Why is this actionable? Windows setup guidance should identify the correct root and use shell-appropriate launchers without changing execution policy.

## Learnings distilled to memory (max 3 new)
| Id | Title | Lesson (do X when Y) | Applies to |
|---|---|---|---|
| None assigned | Windows PowerShell setup | Confirm the repository root and use `.cmd` launchers when PowerShell blocks package-manager scripts. | Student, env-ops |

Other memory updates: None recorded as part of this homework retro.

## Actions
| Action | Owner | Due | Done when |
|---|---|---|---|
| Propose clearer Windows PowerShell setup instructions | Student | Next setup-doc revision | Instructions verify the nested project root and show working `.cmd` commands without changing execution policy |
| Confirm which Git host and author email to use before uploading | Student | Before first push | Private remote is chosen and commit author identity is acceptable |

## Evolution ideas
- Add Windows PowerShell root checks and `.cmd` examples to the setup documentation → EP-001.
