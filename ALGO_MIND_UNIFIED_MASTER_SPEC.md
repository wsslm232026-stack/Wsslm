# ALGO MIND AI — Unified GODCORE Master Specification

**Status:** Approved architecture baseline; planned capabilities are not implementation claims  
**Repository:** `wsslm232026-stack/Wsslm`  
**Target branch:** `repair/algo-mind-core`  
**Audited snapshot:** `0d93553e5d742f55d15e95d855c87ad5f73c3ec1`

## 1. Purpose, authority, and scope

This document defines an incremental target architecture for ALGO MIND AI that preserves the existing Android application identity, Kotlin/Jetpack Compose foundation, and functioning code. It is a specification only: it does not assert that proposed services or modules are implemented, integrated, tested, deployed, or active.

The creator/owner has the highest administrative authority over systems they own and operate. Owner authority is limited to authorized systems, data, and services; it never grants permission to access third-party systems or data without authorization, evade provider controls, or bypass law or contract. No backdoors, hidden accounts, hard-coded credentials, or unauthenticated privileged routes are permitted.

The self-hosted backend is the source of truth for identity, roles, owner status, authorization, licenses, financial state, configuration, and administrative actions. Android is an untrusted client and presentation layer. The approved target has one GODCORE orchestration center and one configured primary AI model behind a provider adapter. Any model failover requires an explicit, reviewed, auditable architecture decision.

Every implementation requires prior explicit approval, a reviewable change record, and truthful test evidence. A capability must not be reported active until implementation and its acceptance tests have passed. This document does not approve any production-code, build, workflow, repository-settings, or deployment changes.

## 2. Current repository baseline and verified status

### Present in audited Android code

- Android module `:app`; Kotlin 2.0.21, Android Gradle Plugin 8.7.3, Jetpack Compose, Java 17, `compileSdk` 35 and `minSdk` 23.
- `MainActivity` starts the Compose `Dashboard`.
- `Dashboard` dispatches local commands through `AiCommandRouter`, presents `ModuleRegistry` metadata, and includes a network status card.
- `AiCommandRouter` handles local commands and delegates numeric analysis to `AnalyticsEngine`.
- `AnalyticsEngine` performs deterministic local statistics on 4-digit inputs.
- `TruthRealityEngine` validates basic evidence fields, HTTPS URL parsing, timestamps, and normalized claim strings. It is not connected to the router in inspected code and cannot establish factual truth or source independence from user-supplied labels alone.
- `InternetSignalBooster` reads Android connectivity capabilities and estimated downstream bandwidth; it cannot strengthen radio signal or create bandwidth.
- A `SecurityPinManager` implementation exists at a malformed filename/path, but no integrated authentication/UI use was found. Do not treat PIN-based protection as active.
- `ModuleRegistry` is descriptive metadata, not runtime service discovery or orchestration.

### Not implemented or not verified

No operational GODCORE orchestrator, selected/connected AI model, self-hosted backend, centralized API, structured remote database, login/RBAC service, MemoryCore, SecurityCore audit service, customer/license service, PaymentCore, OwnerPayoutCore, AutomationCore, or MonitoringCore was found in the inspected source. The current branch build is not verified by this document. No Gradle Wrapper was found in the audited tree.

## 3. Unified target architecture

```text
ALGO MIND AI Android app (untrusted client)
  ├─ Compose Dashboard / Owner Control Center presentation
  ├─ Local input validation and honest status/error display
  └─ HTTPS API client (no master/provider/payment secrets)
                 │ authenticated, bounded, versioned requests
                 ▼
Self-hosted backend (authoritative trust boundary)
  ├─ API Gateway: identity, RBAC, per-resource authorization, rate limits
  ├─ GODCORE: policy-controlled orchestration and module routing
  ├─ One configured primary AI model via an isolated provider adapter
  ├─ TruthRealityEngine     ├─ AnalyticsEngine
  ├─ MemoryCore            ├─ SecurityCore / append-only Audit
  ├─ UserLicenseCore       ├─ PaymentCore
  ├─ OwnerPayoutCore       ├─ AutomationCore
  └─ MonitoringCore
                 │
                 ├─ Structured relational database + versioned migrations
                 ├─ Queue for bounded, idempotent asynchronous work
                 ├─ Secrets manager / key-management service
                 ├─ Encrypted backup + tested restore path
                 └─ Authorized AI, payment, bank, and notification providers
```

The Android app is untrusted. Backend checks are authoritative on every request and operation. GODCORE coordinates work but cannot bypass a module's policy or required owner approval. Provider credentials and master secrets remain server-side.

### Trust boundaries

1. **Android to backend:** TLS-protected authenticated API; validate all client input. Never trust client-supplied role, owner flags, price, entitlement, or tenant ID.
2. **Backend to module:** authenticated service identity, least privilege, typed/versioned contract, bounded request, trace context, and explicit authorization context.
3. **Backend to provider:** narrowly scoped credentials from a secret manager, TLS, verified provider events, timeouts, idempotency, and reconciliation.
4. **Data plane:** tenant/resource authorization at service and persistence boundaries; encryption, retention, and audit controls.
5. **Administrative plane:** owner/admin role separation, step-up verification, approvals, and tamper-evident audit.

## 4. Four architecture-strengthening decisions

1. **Owner authority with explicit trust boundaries.** The owner receives the highest administrative role only within owned and operated systems. Access remains authenticated, authorized, attributable, and bounded; no owner feature permits unauthorized access to third parties.
2. **One GODCORE and one unified module contract.** GODCORE is the sole operational orchestration center. Modules use versioned interfaces and common identity, policy, error, trace, and audit conventions. Do not create duplicate engines or parallel orchestrators. Use one configured primary AI model/provider route at a time behind an adapter; any failover requires explicit approval and an auditable decision.
3. **Authenticated Owner Control Center and recoverable Emergency Master Switch.** Owner operations use strong authentication, server-side authorization, step-up verification for sensitive actions, and audit records. Emergency OFF stops new work and safely drains/cancels eligible jobs without disabling security, audit, or recovery. Recovery is an explicit, health-checked, owner-authorized operation with recorded reason and result.
4. **Official, permissioned, auditable financial flows.** Payments and payouts use approved official provider/bank APIs and satisfy provider and jurisdiction requirements. Never store raw card data. Signed provider events, verification, idempotency, ledger records, reconciliation, and separately authorized payout approval/execution are mandatory.

Cross-cutting governance: every implementation requires prior approval, a change record, and actual test evidence. Status labels distinguish planned, implemented, tested, unavailable, and degraded.

## 5. Standard module communication contract

Proposed backend contract; not yet implemented:

- Versioned module interfaces with typed requests, responses, and capability metadata.
- Each request carries request ID, trace/correlation ID, authenticated principal, server-derived authorization context, deadline, and an idempotency key for mutating operations.
- Standard outcomes distinguish success, invalid input, unauthenticated, forbidden, conflict, unavailable, retryable failure, timeout, and internal failure. Never convert failure to success or return success-shaped placeholder data.
- GODCORE applies policy, routing, timeout, cancellation, and result aggregation. It cannot grant permissions implicitly.
- Events are schema-versioned and privacy-minimized; they contain no credentials or unnecessary personal/payment data.
- Modules do not share broad database credentials or bypass service boundaries. Provider-specific behavior is isolated behind adapters.
- Cross-module side effects require explicit authorization context and traceable audit event.

## 6. Module specifications

All modules below are target designs unless explicitly marked as presently implemented in Section 2. Contracts are illustrative requirements, not implemented APIs.

### 6.1 GODCORE — planned; current router is not GODCORE

- **Responsibility:** central request orchestration, policy evaluation, module selection, deadline/cancellation management, result aggregation, safe degradation, and operational master state.
- **Authority boundary:** may route only after backend identity and authorization; cannot override module policy, owner approvals, tenant boundaries, or provider decisions.
- **Input/output:** authenticated, validated intent and trace/request context in; typed result, source/status, and safe error out. Mutating requests carry idempotency keys and approval references.
- **Dependencies:** API Gateway, SecurityCore policy, module contracts, primary AI adapter when needed, audit service, monitoring/tracing.
- **Security:** default-deny; server-derived principal; bounded model/tool invocation; prompt-injection-safe data handling; no secret exposure; auditable policy decisions.
- **Tests:** authorized/unauthorized routing, tenant boundary, unknown module, timeouts, cancellation, unavailable dependencies, duplicate mutation, audit completeness, and no fabricated success.

### 6.2 TruthRealityEngine — local foundation present, not integrated

- **Responsibility:** validate evidence structure/provenance metadata, normalize claims, and expose uncertainty/conflicts.
- **Authority boundary:** does not decide access, publish arbitrary claims as verified truth, prove factuality, or infer independent sources from hostnames/publisher labels alone.
- **Input/output:** evidence items with source identity/URL, publisher, claim, collection time, excerpt/provenance; assessment with accepted/rejected counts, conflicts, and explicit verification level.
- **Dependencies:** trusted source-acquisition adapter, time source, audit/provenance store where deployed; current implementation uses standard URI and locale utilities only.
- **Security:** accept HTTPS only; validate URL/userinfo and payload bounds; treat excerpts as untrusted; avoid rendering executable content; retain source and collection provenance.
- **Tests:** malformed/insecure URLs, future/invalid timestamps, blank fields, repeated publisher/host, conflicts, normalization, oversized input, and honest status for unverified evidence.

### 6.3 AnalyticsEngine — local implementation present

- **Responsibility:** deterministic calculations and descriptive statistics over validated inputs.
- **Authority boundary:** does not make authorization decisions, claim predictive certainty, or access external data without an approved source adapter.
- **Input/output:** bounded typed datasets in; deterministic summary with validation errors and calculation metadata out.
- **Dependencies:** standard Kotlin runtime today; GODCORE contract and authorized data source only if later integrated.
- **Security:** input size/range limits, no dynamic code execution, no sensitive-data logging, resource limits.
- **Tests:** valid/invalid input, boundaries, duplicates, determinism, overflow/resource limits, locale independence, and disclaimer against guaranteed predictions.

### 6.4 MemoryCore — planned

- **Responsibility:** consent-based, purpose-limited storage, retrieval, update, expiry, export, and deletion of user/system memory.
- **Authority boundary:** accesses only the requesting tenant/user's authorized memory; cannot silently collect, expose across users, or use data for unrelated training.
- **Input/output:** scoped memory command with purpose, consent and retention metadata; authorized provenance-tagged results or explicit absence/denial.
- **Dependencies:** backend identity/RBAC, tenant-isolated database, encryption/KMS, retention/deletion worker, audit.
- **Security:** minimize sensitive fields, encrypt, enforce per-user scope on every query, protect against prompt injection and unauthorized retrieval, honor deletion/retention.
- **Tests:** cross-tenant denial, consent enforcement, retention expiry, deletion, encryption/key access, access audit, and retrieval prompt-injection cases.

### 6.5 SecurityCore — planned

- **Responsibility:** server-side authorization policy, privileged-action step-up checks, security-event handling, secrets/key lifecycle integration, audit coverage, and incident response.
- **Authority boundary:** enforces deny-by-default policy; owner role grants administration only over owned system resources, not third-party access.
- **Input/output:** authenticated principal, action/resource/context in; allow/deny and policy reason code out. Security events yield minimized audit records and alerts.
- **Dependencies:** identity provider, policy store, KMS/secrets manager, audit storage, MonitoringCore.
- **Security:** RBAC plus resource-level checks, session/token revocation, key rotation, rate limiting, secure error responses, least privilege.
- **Tests:** role matrix, resource and tenant authorization, revoked credentials, step-up, default-deny, audit tamper detection, secret-leak scan, and abuse/rate-limit cases.

### 6.6 UserLicenseCore — planned

- **Responsibility:** customer account linkage, product plans, entitlements, expiration, suspension, revocation, and auditable license transitions.
- **Authority boundary:** license status is backend-authoritative; customer cannot assign roles, self-grant entitlements, or modify other accounts.
- **Input/output:** authenticated account and plan/transaction references in; minimal entitlement view and transition result out.
- **Dependencies:** identity, PaymentCore events, database, SecurityCore, audit, approved notification adapter if needed.
- **Security:** tenant isolation, scoped owner/admin actions, event provenance, idempotent transitions, minimal personal data.
- **Tests:** unauthorized changes, expired/revoked access, duplicate event handling, payment/license consistency, tenant isolation, audit history, and recovery after event replay.

### 6.7 PaymentCore — planned

- **Responsibility:** official gateway integration, hosted checkout/tokenization, payment state verification, subscription lifecycle, refunds/disputes, ledger posting, and reconciliation.
- **Authority boundary:** never handles or stores raw PAN/CVV; client-supplied payment success is not authoritative; only verified provider/backend state changes financial records.
- **Input/output:** authenticated purchase intent and server catalog in; provider checkout reference and verified status/receipt metadata out, never secret credentials.
- **Dependencies:** official payment provider, webhook verifier, UserLicenseCore, financial ledger/database, SecurityCore, audit, MonitoringCore.
- **Security:** signed webhook verification, replay defense, idempotency key and unique provider event/transaction IDs, scoped server-side secrets, no sensitive data in logs.
- **Tests:** invalid signature/replay, duplicate delivery, amount/currency mismatch, success/failure/refund/dispute flows, idempotency, ledger invariants, reconciliation mismatch, and sandbox-only integration.

### 6.8 OwnerPayoutCore — planned

- **Responsibility:** controlled payout of eligible reconciled funds to verified owner beneficiaries through authorized official provider/bank rails.
- **Authority boundary:** no direct client execution; separate payout request, approval, execution, and reconciliation; policy limits and required approvals cannot be bypassed by UI.
- **Input/output:** authenticated, step-up-verified request referencing eligible ledger balance and beneficiary in; auditable state and provider reference out.
- **Dependencies:** PaymentCore/ledger, official payout provider, SecurityCore, owner identity/step-up, audit, MonitoringCore.
- **Security:** beneficiary verification, strong authentication, separation of duties/dual approval where appropriate, idempotency, limits, immutable state transitions, no bank credentials in APK/source.
- **Tests:** unauthorized role, unverified beneficiary, insufficient/unreconciled balance, duplicate request, approval separation, provider failure, reconciliation, and sandbox audit completeness.

### 6.9 AutomationCore — planned

- **Responsibility:** execute allowlisted scheduled/event jobs with bounded resources, clear ownership, cancellation, and safe-stop.
- **Authority boundary:** no arbitrary code or unapproved consequential action; requires least privilege and human approval for irreversible/sensitive operations.
- **Input/output:** versioned job specification, authorized trigger, idempotency key, deadline in; job ID, state, bounded result, and audit reference out.
- **Dependencies:** GODCORE, queue, SecurityCore, audit, MonitoringCore, approved modules/providers.
- **Security:** allowlist, isolation, scoped credentials, rate/concurrency limits, bounded retries, no secret-bearing logs, stop/recovery policy.
- **Tests:** unauthorized trigger, duplicate delivery, retry limits, cancellation, emergency OFF, poisoned input, resource exhaustion, and no duplicate financial side effect.

### 6.10 MonitoringCore — planned

- **Responsibility:** health/readiness, metrics, traces, alerts, incident records, backup verification, and documented recovery coordination.
- **Authority boundary:** monitoring does not grant data access or silently mutate financial/user state; recovery actions require explicit policy and owner authorization as appropriate.
- **Input/output:** minimized telemetry and health checks in; operational status, alert/incident IDs, and recovery evidence out.
- **Dependencies:** service instrumentation, audit store, backup system, notification provider, owner/admin authorization.
- **Security:** redact secrets/PII, restrict dashboard access, retain telemetry by policy, protect audit integrity, alert on suspicious access and integrity failures.
- **Tests:** health-check accuracy, alert delivery, secret/PII redaction, audit integrity alert, backup restore drill, RPO/RTO evidence, and recovery authorization.

## 7. Owner Control Center and Emergency Master Switch

The Owner Control Center is a planned authenticated administrative capability, not a bypass. Owner status is assigned and checked server-side. Separate owner, administrator, and customer permissions; administrator permissions are scoped and do not implicitly confer owner authority. Sensitive actions require step-up authentication and explicit confirmation. Apply separation of duties for financial and access-control changes where practical.

Planned, server-authorized functions:

- Master ON/OFF policy and module-level operational state.
- Emergency shutdown: block new work, safely drain/cancel eligible jobs, preserve security/audit/backup/recovery operations, and record incident, actor, time, reason, and outcome.
- Recovery: verify dependencies and integrity, require owner authentication and authorization, record preconditions/checks/outcome, and do not replay unsafe or financial jobs automatically.
- Customer and license administration with scoped permissions and auditable transitions.
- Module status based on observed telemetry, not registry labels or assumptions.
- Transaction/payout review and approval with distinct request, approval, execution, and reconciliation records.
- Searchable, tamper-evident audit history with appropriate access, retention, and privacy controls.

Master OFF must not disable authentication, authorization, security monitoring, audit capture, backups, or the controlled recovery path. Emergency access is still authenticated and audited; it is not a backdoor.

## 8. Required security controls

### A. Android/backend trust boundary

Treat Android as an untrusted client. Backend is authoritative for identity, owner role, access rights, prices, entitlements, financial state, and administrative configuration. Never embed master secrets, payment credentials, provider API keys, or sensitive signing keys in the APK. Validate all client input server-side.

### B. User data isolation

Every user accesses only their own authorized data. Every backend request must check verified identity, role, resource, action, tenant/user ownership, and current entitlement. Derive tenant scope from server-verified identity, not a trusted client parameter. Enforce isolation at service and persistence boundaries and test cross-user reads/writes for every data-bearing module.

### C. Fail-closed authorization and idempotency

Deny sensitive actions if identity, permission, validation, provider status, or required approval is missing, invalid, stale, or uncertain. Mutating payment, payout, license, administration, and automation operations require validated idempotency keys and explicit state transitions. Retries must not duplicate side effects. User interface confirmation is not a substitute for backend authorization.

### D. Append-only audit and tested recovery

Use append-only/tamper-evident audit records for security, administrative, license, payment, and payout events. Record actor, action, target, time, request/trace ID, approval, and outcome while excluding secrets and unnecessary sensitive data. Encrypt backups, define RPO/RTO, restrict backup access, verify backup integrity, and exercise documented restore/recovery procedures. Retain evidence of restore tests; do not claim recovery readiness without successful drills.

### Additional controls

- **API:** HTTPS only; versioned schema; strict validation; bounded payloads; request/trace IDs; per-principal rate limits; explicit timeouts; safe errors.
- **Identity:** standards-based authentication, short-lived credentials and revocation strategy, step-up checks for sensitive owner operations.
- **Secrets/cryptography:** backend secret manager/KMS, scoped keys, rotation, TLS in transit, encryption at rest, secret redaction, and no credentials in Git, logs, test fixtures, or artifacts.
- **Abuse prevention:** resource quotas, monitoring, safe throttling, and non-enumerating errors. Apply legally appropriate risk signals only after privacy review.
- **Resilience:** encrypted backups, defined recovery objectives, restore drills, incident response, and documented rollback. Do not automatically replay irreversible actions.

## 9. Payment, subscriptions, ledger, and owner payouts

- Select official providers only after business, legal, geography, currency, fees, tax, refund, dispute, and operational review.
- Use hosted checkout or provider tokenization. ALGO MIND AI must not collect or persist card PAN/CVV. Store provider references only as permitted by the provider and security review.
- Define supported currencies/products explicitly. Do not claim a country, currency, subscription, or payment rail is available until approved and tested.
- Verify webhook signatures, event timestamps/IDs, replay windows, and provider transaction state. Treat events as at-least-once; deduplicate with unique provider IDs and idempotency keys.
- Maintain an append-only, reconcilable financial ledger with currency, amount, fees, taxes where applicable, refund, dispute, settlement, and payout states. Correct through compensating entries, not silent historical edits.
- Reconcile ledger entries against provider settlement reports; alert and investigate mismatches.
- Payouts use approved rails, verified beneficiaries, limits, separated approval/execution, recorded state, and reconciliation. Never put payout credentials in Android or source control.
- Sandbox tests only until explicit launch approval; production settlement/payouts require legal/provider approval, operational controls, and owner authorization.

## 10. Repository integrity and files to preserve

Preserve the application foundation pending usage and reference review:

- `app/src/main/java/com/algomind/ai/MainActivity.kt`
- `app/src/main/java/com/algomind/ai/ui/Dashboard.kt`
- `app/src/main/java/com/algomind/ai/ui/InternetSignalBooster.kt`
- `app/src/main/java/com/algomind/ai/core/AiCommandRouter.kt`
- `app/src/main/java/com/algomind/ai/core/ModuleRegistry.kt`
- `app/src/main/java/com/algomind/ai/engine/AnalyticsEngine.kt`
- `app/src/main/java/com/algomind/ai/engine/TruthRealityEngine.kt`
- `app/src/main/java/com/algomind/ai/data/AiResponse.kt`
- `app/src/main/AndroidManifest.xml`
- root/app Gradle files, `gradle.properties`, `.gitignore`, and
  `.github/workflows/build-apk.yml`

Audit before moving/removing anything:

- Root-level `MainActivity.kt`, `core/`, `data/`, and `engine/` copies.
- Nested `TruthRealityEngine.kt` under the app source root.
- Malformed names such as `SecurityPinManager.kt,` and manifest-like source files.
- `github/workflows/build-apk.yml` outside the recognized `.github` workflow path.

An unusual path is not by itself proof that file contents are disposable. Do not remove or overwrite files without evidence, owner approval, content/history preservation plan, and rollback route.

## 11. Observed dependencies and Android compatibility

Observed build dependencies/configuration:

- Android Gradle Plugin 8.7.3.
- Kotlin Android and Compose plugins 2.0.21.
- Compose BOM 2024.12.01; Activity Compose 1.10.0; Compose UI, UI tooling preview, Material3.
- Lifecycle runtime KTX 2.8.7.
- Java 17, `compileSdk` 35, `minSdk` 23.
- Workflow actions: `actions/checkout@v4`, `actions/setup-java@v4`,
  `gradle/actions/setup-gradle@v4`, `actions/upload-artifact@v4`.

No backend, database, AI-provider, authentication, payment, or payout libraries were found in inspected app dependencies. Do not add them until provider selection, threat model, license, compatibility, privacy, and tests are reviewed. No Gradle Wrapper was present in the audited tree; the inspected workflow configures Gradle 8.9 separately. Current compile/test success has not been verified by this document task.

## 12. Prioritized implementation roadmap and approval gates

### P0 — integrity and reproducibility
1. Keep work on the repair branch; owner may configure `main` protections through GitHub settings. This document changes no repository security settings.
2. Reproduce the build for an exact branch commit; record actual Gradle/Java versions and full compiler diagnostics.
3. Resolve source duplication/package anomalies only with proof, content-preserving plan, approval, and focused regression tests.
4. Ensure CI covers the repair branch and PRs with least-privilege token permissions.

### P1 — stable local core and contracts
5. Approve module status definitions and versioned module communication contract.
6. Add tests for current router, analytics, truth assessment, edge cases, and resource bounds.
7. Introduce a single GODCORE facade only after its authorization/policy contract is reviewed; integrate existing engines without duplication.
8. Keep unavailable backend-dependent features visibly unavailable.

### P2 — backend and security foundation
9. Approve deployment, database, secret manager, observability, privacy, threat model, retention, data residency, and operational ownership.
10. Implement authentication, RBAC, tenant isolation, API gateway, audit, rate limits, migrations, encrypted backups, and tested restore.
11. Add one primary model-provider adapter with server-side secrets; explicitly approve any failover policy.

### P3 — user and financial capabilities
12. Implement UserLicenseCore with audited entitlement transitions.
13. Select official payment provider(s), applicable jurisdictions/currencies, and subscription/refund/dispute policies.
14. Implement PaymentCore with hosted/tokenized collection, webhook verification/replay protection, idempotency, ledger, and reconciliation.
15. Implement OwnerPayoutCore with strong owner controls, approval separation, official rails, and reconciliation.

### P4 — operations and Android delivery
16. Implement bounded AutomationCore and MonitoringCore, emergency stop/recovery runbooks, and tested restore.
17. Add Owner Control Center UI only against backend-authorized endpoints.
18. Run unit, integration, security, recovery, Android compatibility, and release checks before producing an APK.

Each step requires explicit owner approval, scoped reviewable changes, change record, test evidence, and rollback plan. Approval of this specification is not approval for implementation.

## 13. Testing and acceptance criteria

- Use statuses `PLANNED`, `IMPLEMENTED`, `TESTED`, `UNAVAILABLE`, and `DEGRADED` with documented meanings. `IMPLEMENTED` is not equivalent to `TESTED` or production-ready.
- Do not mark a module active until implementation, integration path, authorization, and required tests pass.
- GODCORE tests cover default-deny authorization, policy/routing, timeouts, cancellation, unavailable modules, and safe failures.
- Isolation tests demonstrate cross-user/tenant read/write denial for every data module.
- Security tests cover RBAC/resource authorization, secret leakage, rate limits, audit tamper detection, backup encryption, restore, and incident recovery.
- Financial tests cover provider signature verification, replay, duplicates, idempotency, refunds/disputes, ledger invariants, settlement reconciliation, and payout approvals using provider sandbox only.
- Automation tests cover allowlisting, least privilege, resource bounds, retries, cancellation, emergency shutdown, and duplicate side effects.
- Android tests cover supported minSdk behavior, API failure states, safe credential handling, and truthful module status.
- Release gates include dependency/security review, manifest/permission review, artifact provenance, owner approval, and rollback procedure.
- Report only commands actually run and recorded outcomes; retain logs and commit identifiers without exposing secrets or personal data.

## 14. Assumptions, risks, and open decisions

### Assumptions

- The existing Android app remains the official client and project identity.
- Backend, database, AI provider/model, payment provider, banking rails, legal jurisdictions, and operational hosting have not yet been selected.
- Owner authority applies only to systems/data under legitimate ownership or delegated control.
- This document is a target specification; repository labels do not establish active functionality.

### Risks

- The repository contains malformed/duplicate source-path artifacts and duplicate root-level copies; cleanup without reference analysis could lose required content or cause build regressions.
- Nested `TruthRealityEngine` has appeared with differing package declarations across commits; an old build failure does not prove the current branch builds.
- No Wrapper was found, reducing build reproducibility; no current build result is asserted here.
- Payment, payout, identity, privacy, consumer-protection, tax, and data-residency obligations depend on provider and jurisdiction and require qualified review.
- One primary AI model creates provider availability risk; any fallback must be explicitly designed, tested, and approved.
- Highest owner authority increases account-takeover and insider-risk impact; strong authentication, recovery safeguards, audit, and financial separation are required.

### Open decisions before implementation

- Self-hosted deployment environment, database engine, backup target, KMS/secrets manager, and monitoring stack.
- Authentication/identity provider, owner recovery model, MFA/step-up method, and admin separation policy.
- Primary AI model/provider, data handling terms, retention, and any approved failover behavior.
- Payment providers, supported currencies/countries, subscription terms, refunds/disputes, tax treatment, and payout rails.
- Data classification, user consent, retention/deletion, residency, and operational RPO/RTO.
- Module API versioning, event transport, service boundaries, and migration/rollback strategy.

## 15. Audit notes and limitations

This specification is based on a read-only GitHub repository snapshot, not a local working tree. Local uncommitted changes, full-history secret scanning, backend deployment, provider contracts, legal jurisdiction, and current build success were not verified. No application code or build/test command was executed as part of this document task.
