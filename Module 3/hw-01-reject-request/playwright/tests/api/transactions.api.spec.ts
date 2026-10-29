import { test as base, expect } from "@fixtures/api";
import { transactions, errorsOf } from "@api";
import { bodyOrThrow } from "@api/http";
import { login } from "@api/auth";
import { env } from "@config";
import { randomAmount } from "@utils/money";

const { involvesUser } = transactions;

/** Seed records this spec needs, read once per test (like the spec's Cypress `beforeEach`). */
const test = base.extend<{ seedIds: { transactionId: string; bankAccountId: string } }>({
  seedIds: async ({ db }, use) => {
    const [transaction, bankAccount] = await Promise.all([db.find("transactions"), db.find("bankaccounts")]);
    await use({ transactionId: transaction.id, bankAccountId: bankAccount.id });
  },
});

test.describe("Transactions API", () => {
  test.describe("GET /transactions", () => {
    test("gets a list of transactions for user (default)", async ({ api, me }) => {
      const res = await transactions.listPersonalTransactions(api);
      expect(res.status).toBe(200);
      expect(involvesUser(me.id)(res.body.results[0])).toBe(true);
    });

    test("gets a list of pending request transactions for user", async ({ api, me }) => {
      const res = await transactions.listPersonalTransactions(api, { requestStatus: "pending" });
      expect(res.status).toBe(200);
      expect(involvesUser(me.id)(res.body.results[0])).toBe(true);
    });

    test("gets a list of pending request transactions for user between a time range", async ({ api, me }) => {
      const res = await transactions.listPersonalTransactions(api, {
        requestStatus: "pending",
        dateRangeStart: new Date("Jan 01 2018").toISOString(),
        dateRangeEnd: new Date("Dec 05 2030").toISOString(),
      });
      expect(res.status).toBe(200);
      expect(involvesUser(me.id)(res.body.results[0])).toBe(true);
    });
  });

  test.describe("GET /transactions/contacts", () => {
    test("gets a list of transactions for users list of contacts, page one", async ({ api }) => {
      const res = await transactions.listContactsTransactions(api);
      expect(res.status).toBe(200);
      expect(res.body.results.length).toBeGreaterThan(1);
    });

    test("gets a list of transactions for users list of contacts, page two", async ({ api }) => {
      const res = await transactions.listContactsTransactions(api, { page: 2 });
      expect(res.status).toBe(200);
      expect(res.body.results.length).toBeGreaterThan(1);
      expect(res.body.pageData.page).toBe(2);
    });
  });

  test.describe("GET /transactions/public", () => {
    test("gets a list of public transactions", async ({ api }) => {
      const res = await transactions.listPublicTransactions(api);
      expect(res.status).toBe(200);
      expect(res.body.results.length).toBeGreaterThan(1);
    });
  });

  test.describe("POST /transactions", () => {
    test("creates a new payment", async ({ api, me, users, seedIds }) => {
      const receiver = users[1];
      const res = await transactions.createTransaction(api, {
        transactionType: "payment",
        source: seedIds.bankAccountId,
        receiverId: receiver.id,
        description: `Payment: ${me.id} to ${receiver.id}`,
        amount: randomAmount(),
        privacyLevel: "public",
      });
      expect(res.status).toBe(200);
      expect(typeof res.body.transaction.id).toBe("string");
      expect(res.body.transaction.status).toBe("complete");
      expect(res.body.transaction.requestStatus).toBeUndefined();
    });

    test("creates a new request", async ({ api, me, users, seedIds }) => {
      const receiver = users[1];
      const res = await transactions.createTransaction(api, {
        transactionType: "request",
        source: seedIds.bankAccountId,
        receiverId: receiver.id,
        description: `Request: ${me.id} from ${receiver.id}`,
        amount: randomAmount(),
        privacyLevel: "public",
      });
      expect(res.status).toBe(200);
      expect(typeof res.body.transaction.id).toBe("string");
      expect(res.body.transaction.status).toBe("pending");
      expect(res.body.transaction.requestStatus).toBe("pending");
    });
  });

  test.describe("PATCH /transactions/:transactionId", () => {
    test("rejecting a request leaves both users' balances unchanged", async ({ api, db, me, users, playwright }) => {
      test.fail(true, "KI-05: rejecting a request currently moves money");

      const receiver = users[1];
      const senderBefore = await db.find("users", { id: me.id });
      const receiverBefore = await db.find("users", { id: receiver.id });
      const bankAccount = await db.find("bankaccounts", { userId: me.id });
      const request = await transactions.createTransaction(api, {
        transactionType: "request",
        source: bankAccount.id,
        receiverId: receiver.id,
        description: `Request: ${me.id} from ${receiver.id}`,
        amount: randomAmount(),
        privacyLevel: "public",
      });
      expect(request.status).toBe(200);

      const receiverApi = await playwright.request.newContext({ baseURL: env.apiURL });
      try {
        bodyOrThrow(await login(receiverApi, receiver.username));
        const rejected = await transactions.updateTransaction(receiverApi, request.body.transaction.id, {
          requestStatus: "rejected",
        });
        expect(rejected.status).toBe(204);
      } finally {
        await receiverApi.dispose();
      }

      const senderAfter = await db.find("users", { id: me.id });
      const receiverAfter = await db.find("users", { id: receiver.id });
      expect(senderAfter.balance).toBe(senderBefore.balance);
      expect(receiverAfter.balance).toBe(receiverBefore.balance);
    });

    test("updates a transaction", async ({ api, seedIds }) => {
      const res = await transactions.updateTransaction(api, seedIds.transactionId, { requestStatus: "rejected" });
      expect(res.status).toBe(204);
    });

    test("errors when an invalid field sent", async ({ api, seedIds }) => {
      const res = await transactions.updateTransaction(api, seedIds.transactionId, {
        notATransactionField: "not a transaction field",
      });
      expect(res.status).toBe(422);
      expect(errorsOf(res)).toHaveLength(1);
    });
  });
});
