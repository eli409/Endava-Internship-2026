# EP-001 — Make Windows PowerShell setup copy-paste safe

| Field | Value |
|---|---|
| Status | proposed |
| Author / date | Student / 2026-10-22 |
| Semver impact | patch — setup documentation and command examples only |
| Guardrail impact | none |
| Evidence | Retro `tap26/reports/retros/retro-20261022-homework.md`; setup experience across commits `d1752f0`–`56134fb`; repeated PowerShell wrapper errors and commands run from the outer ZIP folder |

## Problem
Windows learners using PowerShell can be blocked when commands resolve to `npm.ps1` or `yarn.ps1` under a restrictive execution policy. A ZIP extraction may also contain an outer folder and the actual project root as nested `RWA` folders. Without explicit checks, learners may install in the wrong directory, causing misleading lockfile and package errors. This caused multiple setup retries during the homework session.

## Proposed change
| File | Action | Summary |
|---|---|---|
| `tap26/docs/01-getting-started-vscode.md` | edit | Add a PowerShell-first setup section showing how to verify the project root with `Test-Path`, use `yarn.cmd`, `npm.cmd`, and `npx.cmd`, and avoid changing execution policy. |
| `tap26/labs/lab-01-setup.md` | edit | Add the same Windows-specific root check and exact install commands to the ZIP-download path. |
| `.claude/skills/tap-setup/SKILL.md` | edit | Ensure the PowerShell examples consistently call `.cmd` launchers and explain that `corepack enable` may require elevation only for creating global shims. |

Out of bounds for evolution: `backend/`, `src/`, `data/`, `cypress/`.

## Alternatives considered
- Tell learners to change PowerShell's execution policy — rejected because a machine-wide security setting change is unnecessary for package installation.
- Require Git Bash for every Windows terminal command — rejected because PowerShell can run the project through the `.cmd` launchers and is already the user's default shell.
- Ask learners to guess the correct nested folder — rejected because `Test-Path` checks for expected lockfiles and project files are simple to teach.

## Risks
- Documentation could imply `.cmd` launchers are needed in Git Bash or every shell. Mitigation: label the commands specifically for PowerShell and keep existing POSIX examples for Git Bash/macOS/Linux.
- A copied path may differ between ZIP, clone, or workspace layouts. Mitigation: use relative root checks (`Test-Path .\yarn.lock`, `Test-Path .\playwright\package-lock.json`) and describe expected results.

## How we will test the change
- Follow the documented steps in a fresh Windows PowerShell terminal with a ZIP-style nested folder.
- Confirm both root checks return `True` in the project root and `False` in its parent.
- Run Yarn install with the documented `.cmd` command, install Playwright dependencies from `playwright`, and run the doctor without changing PowerShell execution policy.
- Review the instructions for Git Bash and non-Windows users to ensure their commands remain correct.

## Success metric
A Windows learner starting from a nested ZIP folder completes dependency installation and passes the doctor in one documented attempt, without changing execution policy or installing packages in the parent folder.

## Review
- Recommendation: pending review — not reviewed by an independent agent.
- Overlaps: `env-ops` and `tap-setup` setup guidance.

## Approval & application log
- Student approval: not requested; proposal only.
- Guardrail approval: n/a; no guardrail change proposed.
- Applied: not applied.
- Deviations from plan: none.
