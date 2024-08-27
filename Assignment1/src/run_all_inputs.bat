@echo off

REM Compile the Java program
javac MyClass.java

REM Run the program with all combinations of arguments
for %%i in (A B C D) do (
  for %%j in (true false) do (
    for %%k in (true false) do (
      for %%l in (true false) do (
        for %%m in (true false) do (
          echo Running with: %%i %%j %%k %%l %%m
          java MyClass %%i %%j %%k %%l %%m
        )
      )
    )
  )
)
