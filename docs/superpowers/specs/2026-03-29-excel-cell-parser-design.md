# Excel Cell Value Parser Design

## Overview

Parse Excel files with "常规" (General) cell format, correctly reading original input values including special characters like "$" prefix.

## Problem Statement

When cell format is "General" and user inputs "$100":
- EasyExcel default behavior treats "$" as a currency format prefix
- Reading cell value returns only "100", losing the "$" symbol

## Requirements

| Input | Expected Output |
|-------|-----------------|
| `$100` | `$100` (original string) |
| `100` | `100` (numeric to string) |
| `=SUM(A1,A2)` | Formula result (e.g., sum of A1+A2) |

## Technical Approach

### Solution: Custom CellReadListener with POI Cell Access

Directly access underlying POI Cell object to distinguish cell types and read original values.

### Processing Logic

1. **Formula Cells (`CellType.FORMULA`)**
   - Read cached formula result value
   - Return result as string

2. **Numeric Cells (`CellType.NUMERIC`)**
   - Check if cell has special format prefix (e.g., currency symbol)
   - If yes: use `DataFormatter` to get formatted string (preserves "$")
   - If pure number: convert to string directly

3. **String Cells (`CellType.STRING`)**
   - Read `getStringCellValue()` directly

4. **Blank/Empty Cells**
   - Return empty string or null

### Key Components

```
ExcelParserService
├── parseExcel(InputStream) → List<CellData>
│
CustomCellListener (extends AnalysisEventListener)
├── invoke(Cell cell) → process single cell
├── doAfterAllAnalysed() → completion callback
│
CellData (DTO)
├── rowIndex: int
├── columnIndex: int
├── value: String
├── rawType: String (FORMULA/NUMERIC/STRING/BLANK)
```

### Dependencies

- EasyExcel 3.1.3 (already configured)
- Apache POI (included via EasyExcel dependency)

## Error Handling

- Invalid file format: throw exception with clear message
- Corrupted cells: log warning, return empty value for that cell
- Formula evaluation errors: return "#ERROR" or original formula text

## Testing Strategy

1. Create test Excel file with all three cell types
2. Unit test for each cell type parsing
3. Verify "$100" returns "$100" not "100"
4. Verify formula cells return calculated results