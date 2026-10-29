# BUG-001 — DELETE /contacts/{id} deletes all contacts for an unknown id

| Field | Value |
|---|---|
| Status | confirmed |
| Severity | major — a user loses their saved contacts |
| Priority | P1 — contact data is deleted |
| Area | `DELETE /contacts/:contactId` |
| Reported by / date | Student / 2026-10-21 |
| Related | KI-01 |
| Reproduction rate | 2/2 |

## Environment
- Commit: `d1752f0` · Branch: `hw/02-ki-01`
- App: UI http://localhost:3000 · API http://localhost:3001 · DB reseeded before repro: yes
- Browser / viewport: API only · OS: Windows · Node: `v22.20.0`
- User: `Heath93` (seeded user; password `s3cret`)

## Preconditions
- Local app is running and the Playwright API test has freshly seeded the local test database.
- The authenticated test user has seeded contacts.
- `abc123ABC` is a valid-format contact ID but is not present in the seeded contacts.

## Steps to reproduce
1. Record the contacts returned by `GET /testData/contacts`.
2. Send `DELETE /contacts/abc123ABC` using the authenticated API session.
3. Read `GET /testData/contacts` again.

## Expected result
The API returns `404`, and the contacts remain unchanged — expected behavior for an unknown ID, tracked by KI-01.

## Actual result
The API returns `200` with an empty response object, and the contacts list is empty afterward. The regression test observed this behavior in both runs.

## Evidence
```http
DELETE /contacts/abc123ABC

→ 200
{}
```
- Screenshot / trace: Playwright HTML report from the focused API test
- `data-test` elements involved: `none; API-only issue.

## Impact
A request using an unknown contact ID can remove every saved contact from the database.

## Root cause (confirmed by debugger)
<!-- Optional. Filled only by the debugger after /tap-bug verify. Cite file:line; propose, don't apply. -->
- Location: `{{file:line}}`
- Explanation: {{why it happens}}
- Proposed fix: {{prose or small diff}}

## Regression test
- `playwright/tests/api/social.api.spec.ts` › “DELETE /contacts/:contactId preserves contacts for an unknown id” — asserts `404` and unchanged contacts; marked `test.fail(true, "KI-01: ...")` until the bug is fixed.

## Notes
- Reproduced in two focused Playwright runs against the local seeded database.
- No application fix was attempted.
