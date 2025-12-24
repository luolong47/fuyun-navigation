---
alwaysApply: false
globs: *.ts,*.vue
---
下表总结了您遇到的常见错误及其解决方法：

| 错误类型 | 规范要求 | 快速修复方法 |
| :--- | :--- | :--- |
| **未使用变量** (`no-unused-vars`) | 删除所有未使用的变量、参数、导入。 | 1. 直接删除未使用的变量，如 `error` 参数。<br>2. 如参数需保留（如接口约定），在参数前加下划线：`_error`。<br>3. 使用 `pnpm lint --fix` 尝试自动修复。 |
| **浮空 Promise** (`no-floating-promises`) | Promise 必须被处理（await/.catch/.then）。 | 1. **使用 `await`**：`await someAsyncFunction()`<br>2. **使用 `.catch`**：`someAsyncFunction().catch((e) => console.error(e))`<br>3. **明确忽略**：`void someAsyncFunction()` |
| **类型导入** (`consistent-type-imports`) | 仅作为类型使用的导入，必须使用 `import type`。 | 将 `import { SomeType } from 'foo'` 改为 `import type { SomeType } from 'foo'`。 |

### 🔧 工具与配置建议

除了修复具体错误，合理的工具配置能从根本上规范代码。

1.  **ESLint 配置**：确保您的 `eslint.config.js` 或相关配置文件中启用了严格的 TypeScript 规则。可以参考专业项目的配置，例如启用 `@typescript-eslint/recommended` 规则集，它包含了许多开箱即用的最佳实践规则。
2.  **TypeScript 配置**：在 `tsconfig.json` 中开启严格模式能帮助在编译阶段发现更多问题。
    ```json
    {
      "compilerOptions": {
        "strict": true,
        "noUnusedLocals": true,
        "noUnusedParameters": true,
        "exactOptionalPropertyTypes": true
      }
    }
    ```
3.  **编辑器集成**：在 VS Code 中安装 ESLint 插件，并配置保存时自动修复。
    ```json
    // .vscode/settings.json
    {
      "editor.codeActionsOnSave": {
        "source.fixAll.eslint": true
      }
    }
    ```
### 💡 提升代码质量的额外规范

遵循以下规范可以进一步提升代码的健壮性和可维护性：

-   **避免使用 `any`**：尽量定义精确的类型。如果暂时无法确定类型，可使用 `unknown` 替代 `any`，这要求你在使用前进行类型检查，更安全。
-   **使用可选链（`?.`）和空值合并（`??`）**：它们可以简化代码并安全地处理可能为 `null` 或 `undefined` 的情况。
    ```typescript
    // 简化 a && a.b && a.b.c 这样的代码
    const name = a?.b?.c ?? 'default';
    ```
-   **函数参数设计**：如果函数参数超过3个，建议将它们封装成一个对象参数（Options Object），这样代码会更清晰，也更易于扩展。
    ```typescript
    // 推荐
    function createUser(options: { name: string; age: number; email: string }) {}

    // 不推荐
    function createUser(name: string, age: number, email: string) {}
    ```
-   **命名规范**：
    -   变量和函数使用 `camelCase`（如 `userName`, `getUserInfo`）。
    -   类型和接口使用 `PascalCase`（如 `UserInfo`），并且**不要使用 `I` 前缀**（如直接用 `User` 而非 `IUser`）。

希望这套规范能帮助您高效地解决当前的 lint 错误，并建立起更稳健的 TypeScript 开发习惯。