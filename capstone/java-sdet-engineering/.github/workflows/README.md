# Capstone GitHub workflows

The **course** PR pipeline lives at the repository root:

`.github/workflows/ci.yml`

When this platform is its own GitHub repo, copy a similar workflow here:

1. Compile (`mvn -q test-compile`)
2. Unit tests (no browser)
3. Smoke (`com.company.tests.smoke`)

Do not put tokens in YAML. Use GitHub Actions secrets if a later job needs credentials.
