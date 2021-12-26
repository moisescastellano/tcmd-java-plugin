package tcplugins;
/**
 * @author Ken
 * CPU command information
 */
class Command {

	/**
	 * CPU operation code.
	 */
	private int opCode;

	/**
	 * format string of the assembler instruction.
	 */
	private String formatString;

	/**
	 * format string arguments of the assembler instruction.
	 */
	private String arguments;

	/**
	 * number of bytes of the instruction.
	 */
	private int numBytes;

	/**
	 * CPU cycles (time consumed by that instruction).
	 */
	private int cycles;

	/**
	 * Create a new CPU command.
	 * @param opCodePar CPU operation code
	 * @param formatStringPar format string of assembler instruction
	 * @param argumentsPar the formatString arguments
	 * @param numBytesPar number of bytes of the instruction
	 * @param cyclesPar CPU cycles
	 */
	public Command(final int opCodePar, final String formatStringPar,
			final String argumentsPar, final int numBytesPar, final int cyclesPar) {
		this.opCode = opCodePar;
		this.formatString = formatStringPar;
		this.arguments = argumentsPar;
		this.numBytes = numBytesPar;
		this.cycles = cyclesPar;
	}

	/**
	 * @return CPU operation code
	 */
	public int getOpCode() {
		return opCode;
	}

	/**
	 * @return format string of the assembler instruction.
	 */
	public String getFormatString() {
		return formatString;
	}

	/**
	 * @return format string of the assembler instruction.
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * @return CPU cycles (time consumed by that instruction)
	 */
	public int getCycles() {
		return cycles;
	}

	/**
	 * @return number of bytes of the instruction.
	 */
	public int getNumBytes() {
		return numBytes;
	}

}
